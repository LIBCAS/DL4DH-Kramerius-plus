package cz.inqool.dl4dh.krameriusplus.service.system.job.jobconfig.common;

import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.JobEventService;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.JobStatus;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JobListener implements JobExecutionListener {

    private JobEventService jobEventService;

    @Override
    public void beforeJob(@NonNull JobExecution jobExecution) {
        log.debug("Executing before job:");
        updateJobEventLastExecutionStatus(jobExecution);
    }

    @Override
    public void afterJob(@NonNull JobExecution jobExecution) {
        log.debug("Executing after job:");
        updateJobEventLastExecutionStatus(jobExecution);

        if (jobExecution.getStatus().equals(BatchStatus.COMPLETED)) {
            enqueueNextJobInPlan(jobExecution);
        }
    }

    private void enqueueNextJobInPlan(JobExecution jobExecution) {
        String jobEventId = jobExecution.getJobParameters().getString("jobEventId");

        jobEventService.enqueueNextJobInPlan(jobEventId);
    }

    private void updateJobEventLastExecutionStatus(JobExecution jobExecution) {
        String jobEventId = jobExecution.getJobParameters().getString("jobEventId");
        BatchStatus status = jobExecution.getStatus();
        log.debug("Updating jobEvent {} status to: {}", jobEventId, status);

        jobEventService.updateJobStatus(jobEventId, JobStatus.from(status));
    }

    @Autowired
    public void setJobEventService(JobEventService jobEventService) {
        this.jobEventService = jobEventService;
    }
}
