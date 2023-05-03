package cz.inqool.dl4dh.krameriusplus.core.job.runner;

import cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus;
import cz.inqool.dl4dh.krameriusplus.api.exception.JobException;
import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.job.JobContainer;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstanceService;
import cz.inqool.dl4dh.krameriusplus.core.job.LastLaunch;
import cz.inqool.dl4dh.krameriusplus.core.job.listener.KrameriusJobListenerContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static cz.inqool.dl4dh.krameriusplus.api.exception.JobException.ErrorCode.*;
import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

@Component
@Slf4j
public class JobRunner {

    private KrameriusJobInstanceService jobService;

    private JobContainer jobContainer;

    private KrameriusJobListenerContainer listenerContainer;

    private JobRepository jobRepository;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void run(String krameriusJobInstanceId) {
        KrameriusJobInstance krameriusJobInstance = jobService.findEntity(krameriusJobInstanceId);
        notNull(krameriusJobInstance, () -> new MissingObjectException(KrameriusJobInstance.class, krameriusJobInstanceId));

        try {
            if (!krameriusJobInstance.getExecutionStatus().isStartable()) {
                if (krameriusJobInstance.getExecutionStatus().equals(ExecutionStatus.CANCELLED)) {
                    log.warn("Cannot launch job: " + krameriusJobInstanceId + ", launching jobs under cancelled request is not allowed.");
                    return;
                }
                throw new JobException(krameriusJobInstanceId,
                        "Cannot launch job: " + krameriusJobInstanceId + ", because it is not in a startable status: "
                        + krameriusJobInstance.getExecutionStatus(),
                        NOT_STARTABLE_STATUS);
            }

            Job job = jobContainer.getJob(krameriusJobInstance.getJobType());
            notNull(job, () -> new IllegalArgumentException("No Job found for type: " + krameriusJobInstance.getJobType()));

            JobParameters jobParameters = krameriusJobInstance.getJobParameters();
            JobExecution lastExecution = jobRepository.getLastJobExecution(job.getName(), jobParameters);

            if (lastExecution != null) {
                validateExistingExecution(job, lastExecution, krameriusJobInstanceId);
            }

            job.getJobParametersValidator().validate(krameriusJobInstance.getJobParameters());

            JobExecution jobExecution = createNewJobExecution(krameriusJobInstance);
            if (krameriusJobInstance.getJobInstanceId() == null) {
                jobService.assignInstance(krameriusJobInstance, jobExecution.getJobInstance());
            }

            try {
                listenerContainer.applyBeforeJobListeners(krameriusJobInstance);

                log.info("Job: [" + job + "] launched with the following parameters: [" + jobParameters + "]");
                krameriusJobInstance.setExecutionStatus(ExecutionStatus.STARTED);
                krameriusJobInstance.setLastLaunch(new LastLaunch());
                jobService.getStore().save(krameriusJobInstance);
                job.execute(jobExecution);
                log.info("Job: [" + job + "] completed with the following parameters: [" + jobParameters +
                        "] and the following status: [" + jobExecution.getStatus() + "]");

                jobService.updateStatus(krameriusJobInstance.getId());
            } catch (Exception e) {
                log.info("Job: [" + job + "] failed unexpectedly and fatally with the following parameters: [" + jobParameters + "]", e);

                jobService.updateStatus(krameriusJobInstance.getId(), ExecutionStatus.FAILED_FATALLY);
                throw e;
            }
        } catch (Exception e) {
            jobService.updateStatus(krameriusJobInstance.getId(), ExecutionStatus.FAILED);
            jobService.saveLaunchError(krameriusJobInstance.getId(), e.getMessage());

            log.error("Failed to run KrameriusInstanceJob with ID: " + krameriusJobInstanceId, e);
        } finally {
            listenerContainer.applyAfterJobListeners(krameriusJobInstance);
        }
    }

    private JobExecution createNewJobExecution(KrameriusJobInstance krameriusJobInstance) {
        try {
            return jobRepository.createJobExecution(krameriusJobInstance.getJobType().getName(), krameriusJobInstance.getJobParameters());
        } catch (JobExecutionAlreadyRunningException e) {
            throw new JobException(krameriusJobInstance.getId(), "Job already running", IS_RUNNING, e);
        } catch (JobRestartException e) {
            throw new JobException(krameriusJobInstance.getId(), "Job cannot be restarted", NOT_RESTARTABLE, e);
        } catch (JobInstanceAlreadyCompleteException e) {
            throw new JobException(krameriusJobInstance.getId(), "Job is already completed and cannot be restarted.", IS_COMPLETED);
        }
    }

    private void validateExistingExecution(Job job, JobExecution lastExecution, String krameriusJobInstanceId) {
        if (!job.isRestartable()) {
            throw new JobException(krameriusJobInstanceId, "JobInstance already exists and is not restartable", NOT_RESTARTABLE);
        }
        /*
         * validate here if it has stepExecutions that are UNKNOWN, STARTING, STARTED and STOPPING
         * retrieve the previous execution and check
         */
        for (StepExecution execution : lastExecution.getStepExecutions()) {
            BatchStatus status = execution.getStatus();
            if (status.isRunning() || status == BatchStatus.STOPPING) {
                throw new JobException(krameriusJobInstanceId, "A job execution for this job is already running: " + lastExecution, IS_RUNNING);
            } else if (status == BatchStatus.UNKNOWN) {
                throw new JobException(krameriusJobInstanceId,
                        "Cannot restart step [" + execution.getStepName() + "] from UNKNOWN status. "
                                + "The last execution ended with a failure that could not be rolled back, "
                                + "so it may be dangerous to proceed. Manual intervention is probably necessary.",
                        UNKNOWN_STATUS);
            }
        }
    }

    @Autowired
    public void setJobService(KrameriusJobInstanceService jobService) {
        this.jobService = jobService;
    }

    @Autowired
    public void setJobContainer(JobContainer jobContainer) {
        this.jobContainer = jobContainer;
    }

    @Autowired
    public void setListenerContainer(KrameriusJobListenerContainer listenerContainer) {
        this.listenerContainer = listenerContainer;
    }

    @Autowired
    public void setJobRepository(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }
}
