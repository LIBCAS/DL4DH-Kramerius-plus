package cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent;

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
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class JobEventRunner {

    private JobEventStore jobEventStore;

    private JobLauncher jobLauncher;

    private JobOperator jobOperator;

    private final Map<String, Job> jobs = new HashMap<>();

    private TransactionTemplate transactionTemplate;

    public JobEvent runJob(JobEvent jobEvent) {
        Job jobToRun = jobs.get(jobEvent.getConfig().getKrameriusJob().name());

        try {
            Long newExecutionId;
            if (jobEvent.getLastExecutionId() != null) {
                log.debug("Restarting jobEvent {}", jobEvent);
                newExecutionId = jobOperator.restart(jobEvent.getLastExecutionId());
            } else {
                log.debug("Starting jobEvent {}", jobEvent);
                JobExecution jobExecution = jobLauncher.run(jobToRun, toJobParameters(jobEvent));
                jobEvent.setInstanceId(jobExecution.getJobInstance().getInstanceId());
                newExecutionId = jobExecution.getId();
            }

            jobEvent.setLastExecutionId(newExecutionId);

            log.debug("Storing changes to jobEvent {}", jobEvent);
            return transactionTemplate.execute(t -> jobEventStore.update(jobEvent));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to run job", e);
        }
    }

    private JobParameters toJobParameters(JobEvent jobEvent) {
        JobParametersBuilder builder = new JobParametersBuilder()
                .addString("jobEventId", jobEvent.getId())
                .addString("jobEventName", jobEvent.getJobName())
                .addString("publicationId", jobEvent.getPublicationId())
                .addDate("created", Date.from(jobEvent.getCreated()));

        for (Map.Entry<String, Object> entry : jobEvent.getConfig().getParameters().entrySet()) {
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
    public void setJobEventStore(JobEventStore jobEventStore) {
        this.jobEventStore = jobEventStore;
    }

    @Autowired
    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }
}
