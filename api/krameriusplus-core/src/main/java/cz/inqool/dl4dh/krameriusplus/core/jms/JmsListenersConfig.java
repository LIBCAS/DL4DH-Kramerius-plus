package cz.inqool.dl4dh.krameriusplus.core.jms;

import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.JmsListenerEndpoint;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.JMSException;

@Slf4j
@Configuration
public class JmsListenersConfig implements JmsListenerConfigurer {

    private JobEventService jobEventService;

    private MessageConverter messageConverter;

    @Override
    public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
        registrar.registerEndpoint(createListener(KrameriusJob.ENRICHING_JOB.getQueueName()));
        registrar.registerEndpoint(createListener(KrameriusJob.EXPORTING_JOB.getQueueName()));
    }

    private JmsListenerEndpoint createListener(String destination) {
        SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
        endpoint.setId(destination);
        endpoint.setDestination(destination);
        endpoint.setMessageListener(message -> {
            log.trace("Message received {}", message);
            try {
                JobEvent jobEvent = (JobEvent) messageConverter.fromMessage(message);

                jobEventService.runJob(jobEvent.getId());
            } catch (JMSException e) {
                log.error("Received Exception : " + e);
            }
        });

        return endpoint;
    }

    @Autowired
    public void setMessageConverter(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    @Autowired
    public void setJobEventService(JobEventService jobEventService) {
        this.jobEventService = jobEventService;
    }
}
