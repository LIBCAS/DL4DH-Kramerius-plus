package cz.inqool.dl4dh.krameriusplus.core.jms;

import cz.inqool.dl4dh.krameriusplus.api.job.JobListenerFacade;
import cz.inqool.dl4dh.krameriusplus.core.job.runner.JobRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.api.batch.JobQueue.*;

@Service
@Slf4j
public class JobListenerService implements JobListenerFacade {

    private JobRunner jobRunner;

    private JmsListenerEndpointRegistry registry;

    public void start() {
        Arrays.stream(new String[]{ENRICHMENT_QUEUE,}).forEach( id -> {
            MessageListenerContainer container = registry.getListenerContainer("abc");
            if (container != null) {
                container.start();
            }
            else {
                log.error("JobListenerService.start: Job listener container not found");
            }
        });
    }

    public void stop() {
        Arrays.stream(new String[]{ENRICHMENT_QUEUE}).forEach( id -> {
            MessageListenerContainer container = registry.getListenerContainer("abc");
            if (container != null) {
                container.stop();
            }
            else {
                log.error("JobListenerService.stop: Job listener container not found");
            }
        });
    }

    public String status() {
        return Arrays.stream(new String[]{ENRICHMENT_QUEUE, EXPORT_QUEUE, DEFAULT_QUEUE, PRIORITY_QUEUE}).map(id -> {
            MessageListenerContainer container = registry.getListenerContainer("abc");
            if (container == null) {
                return "Queue listener "+id+" does not exist";
            }
            return container.isRunning() ? "running" : "stopped";
        }).collect(Collectors.joining("\n"));
    }

    @JmsListener(id = ENRICHMENT_QUEUE, destination = ENRICHMENT_QUEUE, concurrency = "3-5")
    public void listenEnrichment(String krameriusJobInstanceId) {
        jobRunner.run(krameriusJobInstanceId);
    }

    @JmsListener(id = EXPORT_QUEUE, destination = EXPORT_QUEUE, concurrency = "1-3")
    public void listenExport(String krameriusJobInstanceId) {
        jobRunner.run(krameriusJobInstanceId);
    }

    @JmsListener(id = DEFAULT_QUEUE, destination = DEFAULT_QUEUE, concurrency = "1-2")
    public void listenDefault(String krameriusJobInstanceId) {
        jobRunner.run(krameriusJobInstanceId);
    }

    @JmsListener(id = PRIORITY_QUEUE, destination = PRIORITY_QUEUE, concurrency = "1")
    public void listenPriority(String krameriusJobInstanceId) {
        jobRunner.run(krameriusJobInstanceId);
    }

    @Autowired
    public void setJobRunner(JobRunner jobRunner) {
        this.jobRunner = jobRunner;
    }

    @Autowired
    public void setRegistry(JmsListenerEndpointRegistry registry) {
        this.registry = registry;
        stop();
    }
}
