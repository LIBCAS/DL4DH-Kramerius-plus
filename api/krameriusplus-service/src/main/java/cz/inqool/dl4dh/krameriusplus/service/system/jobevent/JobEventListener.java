package cz.inqool.dl4dh.krameriusplus.service.system.jobevent;

import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventStore;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobStatus;
import cz.inqool.dl4dh.krameriusplus.service.jms.JmsProducer;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class JobEventListener {

    private JobEventStore jobEventStore;

    private JmsProducer jmsProducer;

    @Transactional
    public void beforeJob(JobEvent jobEvent, JobExecution jobExecution) {
        updateJobEventLastExecutionStatus(jobEvent, JobStatus.from(jobExecution.getStatus().name()));
    }

    @Transactional
    public void afterJob(JobEvent jobEvent, JobExecution jobExecution) {
        BatchStatus jobStatus = jobExecution.getStatus();
        updateJobEventLastExecutionStatus(jobEvent, JobStatus.from(jobStatus.name()));

        if (BatchStatus.COMPLETED.equals(jobStatus)) {
            enqueueNextJobInPlan(jobEvent);
        }
    }

    private void enqueueNextJobInPlan(JobEvent jobEvent) {
        Optional<JobEvent> nextEvent = jobEventStore.findNextToEnqueue(jobEvent.getId());

        nextEvent.ifPresent(this::enqueueJob);
    }

    private void enqueueJob(JobEvent jobEvent) {
        jmsProducer.sendMessage(jobEvent.getConfig().getKrameriusJob(), jobEvent.getId());
    }

    private void updateJobEventLastExecutionStatus(JobEvent jobEvent, JobStatus status) {
        jobEventStore.updateJobStatus(jobEvent.getId(), status);
    }

    @Autowired
    public void setJobEventStore(JobEventStore jobEventStore) {
        this.jobEventStore = jobEventStore;
    }

    @Autowired
    public void setJmsProducer(JmsProducer jmsProducer) {
        this.jmsProducer = jmsProducer;
    }
}
