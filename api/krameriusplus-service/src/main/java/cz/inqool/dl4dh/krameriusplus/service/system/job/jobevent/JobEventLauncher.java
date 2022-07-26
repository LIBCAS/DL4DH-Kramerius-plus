package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent;

import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.core.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.metrics.BatchMetrics;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Custom job launcher implementation (heavily inspired by spring's {@link SimpleJobLauncher}), which updates
 * a JobEvent before running the job synchronously
 */
@Slf4j
@Component
public class JobEventLauncher {

    private JobRepository jobRepository;

    private JobEventService jobEventService;

    private final TaskExecutor taskExecutor = new SyncTaskExecutor();

    private final Map<String, Job> jobs = new HashMap<>();

    public void run(JobEvent jobEvent) throws
            JobExecutionAlreadyRunningException,
            JobRestartException,
            JobInstanceAlreadyCompleteException,
            JobParametersInvalidException {

        JobParameters jobParameters = toJobParameters(jobEvent.toJobParametersMap());
        Assert.notNull(jobEvent, "The JobEvent must not be null.");
        Assert.notNull(jobParameters, "The JobParameters must not be null.");

        Job job = jobs.get(jobEvent.getConfig().getKrameriusJob().name());
        Assert.notNull(job, "The Job must not be null.");

        final JobExecution jobExecution;
        JobExecution lastExecution = jobRepository.getLastJobExecution(job.getName(), jobParameters);
        if (lastExecution != null) {
            if (!job.isRestartable()) {
                throw new JobRestartException("JobInstance already exists and is not restartable");
            }
            /*
             * validate here if it has stepExecutions that are UNKNOWN, STARTING, STARTED and STOPPING
             * retrieve the previous execution and check
             */
            for (StepExecution execution : lastExecution.getStepExecutions()) {
                BatchStatus status = execution.getStatus();
                if (status.isRunning() || status == BatchStatus.STOPPING) {
                    throw new JobExecutionAlreadyRunningException("A job execution for this job is already running: "
                            + lastExecution);
                } else if (status == BatchStatus.UNKNOWN) {
                    throw new JobRestartException(
                            "Cannot restart step [" + execution.getStepName() + "] from UNKNOWN status. "
                                    + "The last execution ended with a failure that could not be rolled back, "
                                    + "so it may be dangerous to proceed. Manual intervention is probably necessary.");
                }
            }
        }

        // Check the validity of the parameters before doing creating anything
        // in the repository...
        job.getJobParametersValidator().validate(jobParameters);

        /*
         * There is a very small probability that a non-restartable job can be
         * restarted, but only if another process or thread manages to launch
         * <i>and</i> fail a job execution for this instance between the last
         * assertion and the next method returning successfully.
         */
        jobExecution = jobRepository.createJobExecution(job.getName(), jobParameters);
        jobEventService.updateRunningJob(jobEvent.getId(), jobExecution.getJobId(), jobExecution.getId());

        try {
            taskExecutor.execute(new Runnable() {

                @Override
                public void run() {
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
                    }
                    catch (Throwable t) {
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
                    }
                    else if (t instanceof Error) {
                        throw (Error) t;
                    }
                    throw new IllegalStateException(t);
                }
            });
        }
        catch (TaskRejectedException e) {
            jobExecution.upgradeStatus(BatchStatus.FAILED);
            if (jobExecution.getExitStatus().equals(ExitStatus.UNKNOWN)) {
                jobExecution.setExitStatus(ExitStatus.FAILED.addExitDescription(e));
            }
            jobRepository.update(jobExecution);
        }

        List<Throwable> failures = jobExecution.getFailureExceptions();
        jobEvent.getDetails().setLastExecutionError(failures.isEmpty() ? null : failures.get(failures.size() - 1));
    }

    private JobParameters toJobParameters(Map<String, Object> jobParametersMap) {
        JobParametersBuilder builder = new JobParametersBuilder();

        for (Map.Entry<String, Object> entry : jobParametersMap.entrySet()) {
            if (entry.getValue() instanceof String) {
                builder.addString(entry.getKey(), (String) entry.getValue());
            } else if (entry.getValue() instanceof Date) {
                builder.addDate(entry.getKey(), (Date) entry.getValue());
            } else if (entry.getValue() instanceof Long) {
                builder.addLong(entry.getKey(), (Long) entry.getValue());
            } else if (entry.getValue() instanceof Double) {
                builder.addDouble(entry.getKey(), (Double) entry.getValue());
            } else {
                builder.addString(entry.getKey(), JsonUtils.toJsonString(entry.getValue()));
            }
        }

        return builder.toJobParameters();
    }

    @Autowired
    public void setJobRepository(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Autowired
    public void setJobEventService(JobEventService jobEventService) {
        this.jobEventService = jobEventService;
    }

    @Autowired
    public void setJobs(Set<Job> jobs) {
        jobs.forEach(job -> this.jobs.put(job.getName(), job));
    }

}
