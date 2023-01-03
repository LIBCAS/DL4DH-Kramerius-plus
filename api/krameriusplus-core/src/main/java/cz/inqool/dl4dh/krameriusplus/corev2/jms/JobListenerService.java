package cz.inqool.dl4dh.krameriusplus.corev2.jms;

import cz.inqool.dl4dh.krameriusplus.corev2.job.runner.JobRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.api.batch.JobQueue.*;

@Component
public class JobListenerService {

    private JobRunner jobRunner;

    @JmsListener(destination = ENRICHMENT_QUEUE, concurrency = "3-5")
    public void listenEnrichment(String krameriusJobInstanceId) {
        jobRunner.run(krameriusJobInstanceId);
    }

    @JmsListener(destination = EXPORT_QUEUE, concurrency = "1-3")
    public void listenExport(String krameriusJobInstanceId) {
        jobRunner.run(krameriusJobInstanceId);
    }

    @JmsListener(destination = DEFAULT_QUEUE, concurrency = "1-2")
    public void listenDefault(String krameriusJobInstanceId) {
        jobRunner.run(krameriusJobInstanceId);
    }

    @JmsListener(destination = PRIORITY_QUEUE, concurrency = "1")
    public void listenPriority(String krameriusJobInstanceId) {
        jobRunner.run(krameriusJobInstanceId);
    }

    @Autowired
    public void setJobRunner(JobRunner jobRunner) {
        this.jobRunner = jobRunner;
    }
}
