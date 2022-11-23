package cz.inqool.dl4dh.krameriusplus.service.system.jobevent;

import cz.inqool.dl4dh.krameriusplus.core.domain.exception.JobException;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.metrics.BatchMetrics;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.core.domain.exception.JobException.ErrorCode.*;

/**
 * Custom job launcher implementation (heavily inspired by spring's {@link SimpleJobLauncher}), which updates
 * a JobEvent before running the job synchronously
 */
@Slf4j
@Component
public class JobEventLauncher {

    private JobRepository jobRepository;

    private final Map<String, Job> jobs = new HashMap<>();

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public JobExecution createExecution(KrameriusJob krameriusJob, JobParameters jobParameters) {
        Job job = jobs.get(krameriusJob.name());

        Assert.notNull(jobParameters, "The JobParameters must not be null.");
        Assert.notNull(job, "The Job must not be null.");

        final JobExecution jobExecution;
        JobExecution lastExecution = jobRepository.getLastJobExecution(job.getName(), jobParameters);
        if (lastExecution != null) {
            if (!job.isRestartable()) {
                throw new JobException("JobInstance already exists and is not restartable", NOT_RESTARTABLE);
            }
            /*
             * validate here if it has stepExecutions that are UNKNOWN, STARTING, STARTED and STOPPING
             * retrieve the previous execution and check
             */
            for (StepExecution execution : lastExecution.getStepExecutions()) {
                BatchStatus status = execution.getStatus();
                if (status.isRunning() || status == BatchStatus.STOPPING) {
                    throw new JobException("A job execution for this job is already running: "
                            + lastExecution, IS_RUNNING);
                } else if (status == BatchStatus.UNKNOWN) {
                    throw new JobException(
                            "Cannot restart step [" + execution.getStepName() + "] from UNKNOWN status. "
                                    + "The last execution ended with a failure that could not be rolled back, "
                                    + "so it may be dangerous to proceed. Manual intervention is probably necessary.",
                            UNKNOWN_STATUS);
                }
            }
        }

        // Check the validity of the parameters before doing creating anything
        // in the repository...

        try {
            job.getJobParametersValidator().validate(jobParameters);
        } catch (JobParametersInvalidException e) {
            throw new JobException(e.getMessage(), INVALID_JOB_PARAMETERS);
        }

        /*
         * There is a very small probability that a non-restartable job can be
         * restarted, but only if another process or thread manages to launch
         * <i>and</i> fail a job execution for this instance between the last
         * assertion and the next method returning successfully.
         */
        try {
            jobExecution = jobRepository.createJobExecution(job.getName(), jobParameters);
        } catch (JobInstanceAlreadyCompleteException e) {
            throw new JobException(e.getMessage(), IS_COMPLETE);
        } catch (JobExecutionAlreadyRunningException e) {
            throw new JobException(e.getMessage(), IS_RUNNING);
        } catch (JobRestartException e) {
            throw new JobException(e.getMessage(), NOT_RESTARTABLE);
        }

        return jobExecution;
    }

    public void runJob(KrameriusJob krameriusJob, JobExecution jobExecution) {
        Job job = jobs.get(krameriusJob.name());
        JobParameters jobParameters = jobExecution.getJobParameters();

        try {
            if (log.isInfoEnabled()) {
                log.info("Job: [" + job + "] launched with the following parameters: [" + jobParameters
                        + "]");
            }
            job.execute(jobExecution);
            if (log.isInfoEnabled()) {
                Duration jobExecutionDuration = BatchMetrics.calculateDuration(jobExecution.getStartTime(), jobExecution.getEndTime());
                log.info("Job: [" + job + "] completed with the following parameters: [" + jobParameters
                        + "] and the following status: [" + jobExecution.getStatus() + "]"
                        + (jobExecutionDuration == null ? "" : " in " + BatchMetrics.formatDuration(jobExecutionDuration)));
            }
        } catch (Throwable t) {
            if (log.isInfoEnabled()) {
                log.info("Job: [" + job
                        + "] failed unexpectedly and fatally with the following parameters: [" + jobParameters
                        + "]", t);
            }
            rethrow(t);
        }
    }

    private void rethrow(Throwable t) {
        if (t instanceof RuntimeException) {
            throw (RuntimeException) t;
        } else if (t instanceof Error) {
            throw (Error) t;
        }
        throw new IllegalStateException(t);
    }

    @Autowired
    public void setJobRepository(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Autowired
    public void setJobs(Collection<Job> jobs) {
        jobs.forEach(job -> this.jobs.put(job.getName(), job));
    }
}
