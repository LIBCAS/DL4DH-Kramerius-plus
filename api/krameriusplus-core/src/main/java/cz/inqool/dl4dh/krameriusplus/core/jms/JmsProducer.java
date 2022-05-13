package cz.inqool.dl4dh.krameriusplus.core.jms;

import cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.dto.JobEventRunDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JmsProducer {

    private JmsTemplate jmsTemplate;

    public void sendMessage(JobEventRunDto jobEvent) {
        try{
            log.debug("Attempting Send message : " + jobEvent.toString());
            jmsTemplate.convertAndSend(jobEvent.getKrameriusJob().getQueueName(), jobEvent,
                    message -> {
                        message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 500);
                        return message;
                    });
        } catch(Exception e) {
            log.error("Received Exception during send Message: ", e);
        }
    }

    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
}
