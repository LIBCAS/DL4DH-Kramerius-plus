package cz.inqool.dl4dh.krameriusplus.service.jms;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.service.system.jobevent.dto.JobEventRunDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JmsProducer {

    private JmsTemplate jmsTemplate;

    public void sendMessage(KrameriusJob krameriusJob, String jobEventId) {
        try {
            log.debug("Attempting Send message : " + jobEventId);
            jmsTemplate.convertAndSend(krameriusJob.getQueueName(), new JobEventRunDto(jobEventId),
                    message -> {
                        message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 500);
                        return message;
                    });
        } catch (Exception e) {
            log.error("Received Exception during send Message: ", e);
        }
    }

    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
}
