package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.DatedService;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.*;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
import cz.inqool.dl4dh.krameriusplus.service.jms.JmsProducer;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto.JobEventDetailDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto.JobEventMapper;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.JobPlan;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.JobPlanStore;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

@Service
@Slf4j
public class JobEventService implements DatedService<JobEvent, JobEventCreateDto, JobEventDto> {

    @Getter
    private JobEventStore store;

    private JobPlanStore jobPlanStore;

    @Getter
    private JobEventMapper mapper;

    private JmsProducer jmsProducer;

    private JobExplorer jobExplorer;

    private TransactionTemplate transactionTemplate;

    public JobEventDto createAndEnqueue(JobEventCreateDto createDto) {
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        JobEvent jobEvent = transactionTemplate.execute(t -> store.create(getMapper().fromCreateDto(createDto)));

        enqueueJob(jobEvent);

        return mapper.toDto(jobEvent);
    }

    public void enqueueNextJobInPlan(String lastExecutedJobEventId) {
        JobPlan jobPlan = jobPlanStore.findByJobEvent(lastExecutedJobEventId);

        if (jobPlan != null) {
            Optional<JobEvent> jobEvent = jobPlan.getNextToExecute();

            jobEvent.ifPresent(this::enqueueJob);
        }
    }

    public void restart(String jobEventId) {
        JobEvent jobEvent = store.find(jobEventId);
        notNull(jobEvent, () -> new MissingObjectException(JobEvent.class, jobEventId));

        if (JobStatus.FAILED.equals(jobEvent.getDetails().getLastExecutionStatus())) {
            enqueueJob(jobEvent);
        } else {
            throw new IllegalStateException("Only jobs with lastExecutionStatus='FAILED' can be restarted " +
                    "(JobEvent's lastExecutionStatus is " + jobEvent.getDetails().getLastExecutionStatus() + ")");
        }
    }

    public JobEventDetailDto findDetailed(@NonNull String id) {
        JobEvent jobEvent = store.find(id);
        notNull(jobEvent, () -> new MissingObjectException(JobEvent.class, id));

        List<JobExecution> executions = new ArrayList<>();

        if (jobEvent.getInstanceId() != null) {
            JobInstance instance = jobExplorer.getJobInstance(jobEvent.getInstanceId());

            executions = jobExplorer.getJobExecutions(Objects.requireNonNull(instance));
        }

        return mapper.toDetailDto(jobEvent, executions);
    }

    @Transactional
    public void updateJobStatus(String jobEventId, JobStatus status) {
        store.updateJobStatus(jobEventId, status);
    }

    @Transactional
    public void updateRunningJob(String jobEventId, Long jobInstanceId, Long jobExecutionId, String failure) {
        store.updateJobRun(jobEventId, jobInstanceId, jobExecutionId, failure);
    }

    public QueryResults<JobEventDto> listEnrichingJobs(JobEventFilter jobEventFilter, int page, int pageSize) {
        if (jobEventFilter.getKrameriusJobs() == null || jobEventFilter.getKrameriusJobs().isEmpty()) {
            jobEventFilter.setKrameriusJobs(KrameriusJob.getEnrichingJobs());
        }

        QueryResults<JobEvent> result = store.listJobs(jobEventFilter, page, pageSize);

        return new QueryResults<>(mapper.toDtoList(result.getResults()), result.getLimit(), result.getOffset(), result.getTotal());
    }

    public QueryResults<JobEventDto> listExportingJobs(JobEventFilter jobEventFilter, int page, int pageSize) {
        if (jobEventFilter.getKrameriusJobs() == null || jobEventFilter.getKrameriusJobs().isEmpty()) {
            jobEventFilter.setKrameriusJobs(KrameriusJob.getExportingJobs());
        }

        QueryResults<JobEvent> result = store.listJobs(jobEventFilter, page, pageSize);

        return new QueryResults<>(mapper.toDtoList(result.getResults()), result.getLimit(), result.getOffset(), result.getTotal());
    }

    public void enqueueJob(JobEvent jobEvent) {
        jmsProducer.sendMessage(jobEvent.getConfig().getKrameriusJob().getQueueName(), mapper.toRunDto(jobEvent));
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
    public void setJobPlanStore(JobPlanStore jobPlanStore) {
        this.jobPlanStore = jobPlanStore;
    }

    @Autowired
    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }
}
