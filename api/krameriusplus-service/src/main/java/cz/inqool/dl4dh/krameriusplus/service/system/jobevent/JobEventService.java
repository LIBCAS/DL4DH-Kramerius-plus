package cz.inqool.dl4dh.krameriusplus.service.system.jobevent;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.DatedService;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.JobException;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventFilter;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventStore;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobStatus;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.executions.StepRunReport;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.executions.StepRunReportStore;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.service.jms.JmsProducer;
import cz.inqool.dl4dh.krameriusplus.service.system.jobevent.dto.JobEventDetailDto;
import cz.inqool.dl4dh.krameriusplus.service.system.jobevent.dto.JobEventMapper;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cz.inqool.dl4dh.krameriusplus.core.domain.exception.JobException.ErrorCode.NOT_RUNNING;
import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

@Service
@Slf4j
public class JobEventService implements DatedService<JobEvent, JobEventCreateDto, JobEventDto> {

    @Getter
    private JobEventStore store;

    @Getter
    private JobEventMapper mapper;

    private JmsProducer jmsProducer;

    private JobExplorer jobExplorer;

    private JobEventLauncher jobEventLauncher;

    private JobOperator jobOperator;

    private JobEventListener jobEventListener;

    private StepRunReportStore stepRunReportStore;

    @Override
    @Transactional
    public JobEvent create(@NonNull JobEvent jobEvent) {
        JobExecution jobExecution = jobEventLauncher.createExecution(
                jobEvent.getConfig().getKrameriusJob(),
                mapper.toJobParameters(jobEvent));

        jobEvent.setInstanceId(jobExecution.getJobId());
        jobEvent.getDetails().setLastExecutionId(jobExecution.getId());

        return store.create(jobEvent);
    }

    @Transactional
    public void saveError(String jobEventId, Long stepExecutionId, Throwable throwable) {
        JobEvent jobEvent = findEntity(jobEventId);
        StepRunReport report = jobEvent.getStepRunReports()
                .stream()
                .filter(r -> r.getStepExecutionId().equals(stepExecutionId))
                .findFirst()
                .orElseGet(() -> {
                    StepRunReport newReport = new StepRunReport();
                    newReport.setJobEvent(jobEvent);
                    newReport.setStepExecutionId(stepExecutionId);

                    return stepRunReportStore.create(newReport);
                });

        report.addError(throwable);

        stepRunReportStore.update(report);
    }

    public void run(String jobEventId) {
        JobEvent jobEvent = findEntity(jobEventId);

        JobExecution jobExecution = jobExplorer.getJobExecution(jobEvent.getDetails().getLastExecutionId());
        notNull(jobExecution, () -> new MissingObjectException(JobExecution.class, String.valueOf(jobEvent.getDetails().getLastExecutionId())));

        try {
            jobEventListener.beforeJob(jobEvent, jobExecution);
            jobEventLauncher.runJob(jobEvent.getConfig().getKrameriusJob(), jobExecution);
        } catch (Exception e) {
            jobEvent.getDetails().setRunErrorMessage(e.getMessage());
            jobEvent.getDetails().setRunErrorStackTrace(ExceptionUtils.getStackTrace(e));
            store.update(jobEvent);

            throw new IllegalStateException("Failed to run job", e);
        } finally {
            jobEventListener.afterJob(jobEvent, jobExecution);
        }
    }

    @Transactional
    public void restart(String jobEventId) {
        JobEvent jobEvent = findEntity(jobEventId);

        JobExecution jobExecution = jobEventLauncher.createExecution(
                jobEvent.getConfig().getKrameriusJob(),
                mapper.toJobParameters(jobEvent));

        jobEvent.getDetails().setLastExecutionStatus(JobStatus.from(jobExecution.getStatus().name()));
        jobEvent.getDetails().setLastExecutionExitCode(null);
        jobEvent.getDetails().setLastExecutionExitDescription(null);
        jobEvent.getDetails().setLastExecutionId(jobExecution.getId());
        jobEvent = store.update(jobEvent);

        enqueueJob(jobEvent);
    }

    public void stop(String jobEventId) {
        JobEvent jobEvent = findEntity(jobEventId);

        try {
            jobOperator.stop(jobEvent.getDetails().getLastExecutionId());
        } catch (JobExecutionNotRunningException e) {
            throw new JobException(String.format("JobEvent with id=%s has no execution running", jobEventId),
                    NOT_RUNNING);
        } catch (NoSuchJobExecutionException e) {
            throw new IllegalStateException("No such Job");
        }
    }

    public JobEventDetailDto findDetailed(@NonNull String id) {
        JobEvent jobEvent = findEntity(id);
        notNull(jobEvent, () -> new MissingObjectException(JobEvent.class, id));

        List<JobExecution> executions = new ArrayList<>();

        if (jobEvent.getInstanceId() != null) {
            JobInstance instance = jobExplorer.getJobInstance(jobEvent.getInstanceId());

            executions = jobExplorer.getJobExecutions(Objects.requireNonNull(instance));
        }

        return mapper.toDetailDto(jobEvent, executions);
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
        jmsProducer.sendMessage(jobEvent.getConfig().getKrameriusJob(), jobEvent.getId());
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
    public void setJobEventLauncher(JobEventLauncher jobEventLauncher) {
        this.jobEventLauncher = jobEventLauncher;
    }

    @Autowired
    public void setJobOperator(JobOperator jobOperator) {
        this.jobOperator = jobOperator;
    }

    @Autowired
    public void setJobEventListener(JobEventListener jobEventListener) {
        this.jobEventListener = jobEventListener;
    }

    @Autowired
    public void setStepRunReportStore(StepRunReportStore stepRunReportStore) {
        this.stepRunReportStore = stepRunReportStore;
    }
}
