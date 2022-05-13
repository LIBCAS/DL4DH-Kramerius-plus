package cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent;

import cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.dto.JobEventRunDto;
import cz.inqool.dl4dh.krameriusplus.core.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class JobEventRunner {

    private JobEventService jobEventService;

    private JobLauncher jobLauncher;

    private JobOperator jobOperator;

    private final Map<String, Job> jobs = new HashMap<>();

    public void runJob(JobEventRunDto jobEvent) {
        Job jobToRun = jobs.get(jobEvent.getKrameriusJob().name());

        try {
            if (jobEvent.getLastExecutionId() != null) {
                log.debug("Restarting jobEvent {}", jobEvent);
                jobEvent.setLastExecutionId(jobOperator.restart(jobEvent.getLastExecutionId()));
            } else {
                log.debug("Starting jobEvent {}", jobEvent);
                JobExecution jobExecution = jobLauncher.run(jobToRun, toJobParameters(jobEvent.getJobParametersMap())); // runs before job, which updates jobStatus=STARTED
                jobEvent.setInstanceId(jobExecution.getJobInstance().getInstanceId());
                jobEvent.setLastExecutionId(jobExecution.getId());
            }

            log.debug("Storing changes to jobEvent {}", jobEvent);

            jobEventService.updateRunningJob(jobEvent); // updates jobStatus = null (and cancel STARTED status)
        } catch (Exception e) {
            throw new IllegalStateException("Failed to run job", e);
        }
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
    public void setJobs(Set<Job> jobs) {
        jobs.forEach(job -> this.jobs.put(job.getName(), job));
    }

    @Autowired
    public void setJobService(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    @Autowired
    public void setJobOperator(JobOperator jobOperator) {
        this.jobOperator = jobOperator;
    }

    @Autowired
    public void setJobEventService(JobEventService jobEventService) {
        this.jobEventService = jobEventService;
    }
}
