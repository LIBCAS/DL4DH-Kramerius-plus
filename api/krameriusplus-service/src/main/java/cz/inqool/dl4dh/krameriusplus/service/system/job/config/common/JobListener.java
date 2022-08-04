package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common;

import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventStore;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobStatus;
import cz.inqool.dl4dh.krameriusplus.service.jms.JmsProducer;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.JobPlan;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.JobPlanStore;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.JOB_EVENT_ID;

@Component
@Slf4j
public class JobListener implements JobExecutionListener {

    private JobPlanStore jobPlanStore;

    private JmsProducer jmsProducer;

    private JobEventStore jobEventStore;

    private TransactionTemplate transactionTemplate;

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
        String jobEventId = jobExecution.getJobParameters().getString(JOB_EVENT_ID);

        JobPlan jobPlan = jobPlanStore.findByJobEvent(jobEventId);

        if (jobPlan != null) {
            Optional<JobEvent> jobEventOptional = jobPlan.getNextToExecute();

            jobEventOptional.ifPresent(jobEvent ->
                    jmsProducer.sendMessage(jobEvent.getConfig().getKrameriusJob(), jobEvent.getId()));
        }
    }

    private void updateJobEventLastExecutionStatus(JobExecution jobExecution) {
        String jobEventId = jobExecution.getJobParameters().getString(JOB_EVENT_ID);
        BatchStatus status = jobExecution.getStatus();
        log.debug("Updating jobEvent {} status to: {}", jobEventId, status);

        transactionTemplate.executeWithoutResult(t ->
                jobEventStore.updateJobStatus(jobEventId, JobStatus.from(status.name())));
    }

    @Autowired
    public void setJobEventStore(JobEventStore jobEventStore) {
        this.jobEventStore = jobEventStore;
    }

    @Autowired
    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    @Autowired
    public void setJobPlanStore(JobPlanStore jobPlanStore) {
        this.jobPlanStore = jobPlanStore;
    }

    @Autowired
    public void setJmsProducer(JmsProducer jmsProducer) {
        this.jmsProducer = jmsProducer;
    }
}
