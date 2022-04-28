package cz.inqool.dl4dh.krameriusplus.core.system.jobevent;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.service.DatedService;
import cz.inqool.dl4dh.krameriusplus.core.jms.JmsProducer;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.create.JobEventCreateDto;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

@Service
public class JobEventService implements DatedService<JobEvent, JobEventCreateDto, JobEventDto> {

    @Getter
    private JobEventStore store;

    @Getter
    private JobEventMapper mapper;

    private JmsProducer jmsProducer;

    private JobExplorer jobExplorer;

    private TransactionTemplate transactionTemplate;

    @Override
    public JobEventDto create(@Valid @NonNull JobEventCreateDto createDto) {
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        JobEvent jobEvent = transactionTemplate.execute(t -> getStore().create(getMapper().fromCreateDto(createDto)));

        jmsProducer.sendMessage(jobEvent);

        return getMapper().toDto(jobEvent);
    }

    @Override
    public JobEventDto find(@NonNull String id) {
        JobEvent jobEvent = store.find(id);
        notNull(jobEvent, () -> new MissingObjectException(JobEvent.class, id));

        List<JobExecution> executions = new ArrayList<>();

        if (jobEvent.getInstanceId() != null) {
            JobInstance instance = jobExplorer.getJobInstance(jobEvent.getInstanceId());
            executions = jobExplorer.getJobExecutions(Objects.requireNonNull(instance));
        }

        return mapper.toDto(jobEvent, executions);
    }

    @Transactional
    public void updateJobStatus(String jobEventId, BatchStatus status) {
        store.updateJobStatus(jobEventId, status);
    }

    public QueryResults<JobEventDto> listEnrichingJobs(String publicationId, int page, int pageSize) {
        QueryResults<JobEvent> result = store.listJobsByType(KrameriusJob.getEnrichingJobs(), publicationId, page, pageSize);

        return new QueryResults<>(mapToDto(result.getResults()), result.getLimit(), result.getOffset(), result.getTotal());
    }

    public QueryResults<JobEventDto> listExportingJobs(String publicationId, int page, int pageSize) {
        QueryResults<JobEvent> result = store.listJobsByType(KrameriusJob.getExportingJobs(), publicationId, page, pageSize);

        return new QueryResults<>(mapToDto(result.getResults()), result.getLimit(), result.getOffset(), result.getTotal());
    }

    private List<JobEventDto> mapToDto(List<JobEvent> jobEvents) {
        return jobEvents.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Autowired
    public void setJobEventStore(JobEventStore jobEventStore) {
        this.store = jobEventStore;
    }

    @Autowired
    public void setMapper(JobEventMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setJmsProducer(JmsProducer jmsProducer) {
        this.jmsProducer = jmsProducer;
    }


    @Autowired
    public void setJobExplorer(JobExplorer jobExplorer) {
        this.jobExplorer = jobExplorer;
    }

    @Autowired
    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }
}
