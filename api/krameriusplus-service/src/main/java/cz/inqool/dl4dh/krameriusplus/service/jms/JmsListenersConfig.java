package cz.inqool.dl4dh.krameriusplus.service.jms;

import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.JobEventRunner;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto.JobEventRunDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.JmsListenerEndpoint;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.JMSException;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventQueue.ENRICHMENT_QUEUE;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventQueue.EXPORT_QUEUE;

@Slf4j
@Configuration
public class JmsListenersConfig implements JmsListenerConfigurer {

    private JobEventRunner jobEventRunner;

    private MessageConverter messageConverter;

    @Override
    public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
        registrar.registerEndpoint(createListener(ENRICHMENT_QUEUE.getQueueName()));
        registrar.registerEndpoint(createListener(EXPORT_QUEUE.getQueueName()));
    }

    private JmsListenerEndpoint createListener(String destination) {
        SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
        endpoint.setId(destination);
        endpoint.setDestination(destination);
        endpoint.setMessageListener(message -> {
            log.debug("Message received {}", message);
            try {
                JobEventRunDto jobEvent = (JobEventRunDto) messageConverter.fromMessage(message);

                log.debug("Message content: {}", jobEvent);
                jobEventRunner.runJob(jobEvent);
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
    public void setJobEventRunner(JobEventRunner jobEventRunner) {
        this.jobEventRunner = jobEventRunner;
    }
}
