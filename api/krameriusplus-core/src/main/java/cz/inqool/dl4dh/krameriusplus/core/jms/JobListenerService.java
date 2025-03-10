package cz.inqool.dl4dh.krameriusplus.core.jms;

import cz.inqool.dl4dh.krameriusplus.api.job.JobListenerFacade;
import cz.inqool.dl4dh.krameriusplus.core.job.runner.JobRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.api.batch.JobQueue.*;

@Service
@Slf4j
public class JobListenerService implements JobListenerFacade {

    private JobRunner jobRunner;

    private JmsListenerEndpointRegistry registry;

    @Value("${system.enrichment.time.from:00:01}")
    private String enrichFrom;

    @Value("${system.enrichment.time.to:05:00}")
    private String enrichTo;

    private Calendar stringToDate(String time) {
        Calendar calendar = Calendar.getInstance();
        String[] parts = time.split(":");
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
        calendar.set(Calendar.SECOND, Integer.parseInt(parts[2]));
        return calendar;
    }

    @Scheduled(fixedDelay = 5000)
    public void refresh() {
        Calendar calendarFrom = stringToDate(enrichFrom+":00");
        Calendar calendarTo = stringToDate(enrichTo+":59");
        Calendar calendarNow = Calendar.getInstance();
        Date now = calendarNow.getTime();

        if (calendarFrom.after(calendarTo)) {
            calendarTo.add(Calendar.DATE, 1);
        }

        if (now.after(calendarFrom.getTime()) && now.before(calendarTo.getTime())) {
            start();
        }
        else {
            stop();
        }
    }

    public void start() {
        Arrays.stream(new String[]{ENRICHMENT_QUEUE,}).forEach( id -> {
            MessageListenerContainer container = registry.getListenerContainer(id);
            if (container != null) {
                if (!container.isRunning()) {
                    container.start();
                }
            }
            else {
                log.error("JobListenerService.start: Job listener "+ id +" container not found");
            }
        });
    }

    public void stop() {
        Arrays.stream(new String[]{ENRICHMENT_QUEUE}).forEach( id -> {
            MessageListenerContainer container = registry.getListenerContainer(id);
            if (container != null) {
                if (container.isRunning()) {
                    container.stop();
                }
            }
            else {
                log.error("JobListenerService.stop: Job listener "+ id +" container not found");
            }
        });
    }

    public Map<String, Boolean> status() {
        return Arrays.stream(new String[]{ENRICHMENT_QUEUE, EXPORT_QUEUE, DEFAULT_QUEUE, PRIORITY_QUEUE})
                .collect(Collectors.toMap(id -> id, id -> {
                    MessageListenerContainer container = registry.getListenerContainer(id);
                    if (container == null) {
                        return false;
                    }
                    return container.isRunning();
                }));
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
