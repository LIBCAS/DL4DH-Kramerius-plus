package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.DatedService;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventStore;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobStatus;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.service.jms.JmsProducer;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto.JobEventCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto.JobEventDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto.JobEventMapper;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto.JobEventRunDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.JobPlan;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.JobPlanStore;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

@Service
public class JobEventService implements DatedService<JobEvent, JobEventCreateDto, JobEventDto> {

    @Getter
    private JobEventStore store;

    private JobPlanStore jobPlanStore;

    @Getter
    private JobEventMapper mapper;

    private JmsProducer jmsProducer;

    private JobExplorer jobExplorer;

    public void enqueueJob(String jobEventId) {
        JobEvent jobEvent = store.find(jobEventId);
        notNull(jobEvent, () -> new MissingObjectException(JobEvent.class, jobEventId));

        enqueueJob(jobEvent);
    }

    public void enqueueNextJobInPlan(String lastExecutedJobEventId) {
        JobPlan jobPlan = jobPlanStore.findByJobEvent(lastExecutedJobEventId);

        if (jobPlan != null) {
            Optional<JobEvent> jobEvent = jobPlan.getNextToExecute();

            jobEvent.ifPresent(event -> enqueueJob(event.getId()));
        }
    }

    public void restart(String jobEventId) {
        JobEvent jobEvent = store.find(jobEventId);
        notNull(jobEvent, () -> new MissingObjectException(JobEvent.class, jobEventId));

        if (JobStatus.FAILED.equals(jobEvent.getLastExecutionStatus())) {
            enqueueJob(jobEvent);
        } else {
            throw new IllegalStateException("Only jobs with lastExecutionStatus='FAILED' can be restarted " +
                    "(JobEvent's lastExecutionStatus is " + jobEvent.getLastExecutionStatus() + ")");
        }
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
    public void updateJobStatus(String jobEventId, JobStatus status) {
        store.updateJobStatus(jobEventId, status);
    }

    @Transactional
    public void updateRunningJob(JobEventRunDto jobEvent) {
        store.updateJobRun(jobEvent.getId(), jobEvent.getInstanceId(), jobEvent.getLastExecutionId());
    }

    public QueryResults<JobEventDto> listEnrichingJobs(String publicationId, KrameriusJob krameriusJob, int page, int pageSize) {
        QueryResults<JobEvent> result = store.listJobsByType(
                krameriusJob == null ? KrameriusJob.getEnrichingJobs() : Set.of(krameriusJob),
                publicationId,
                page,
                pageSize);

        return new QueryResults<>(mapToDto(result.getResults()), result.getLimit(), result.getOffset(), result.getTotal());
    }

    public QueryResults<JobEventDto> listExportingJobs(String publicationId, int page, int pageSize) {
        QueryResults<JobEvent> result = store.listJobsByType(KrameriusJob.getExportingJobs(), publicationId, page, pageSize);

        return new QueryResults<>(mapToDto(result.getResults()), result.getLimit(), result.getOffset(), result.getTotal());
    }

    private void enqueueJob(JobEvent jobEvent) {
        jmsProducer.sendMessage(mapper.toRunDto(jobEvent));
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
    public void setJobPlanStore(JobPlanStore jobPlanStore) {
        this.jobPlanStore = jobPlanStore;
    }
}
