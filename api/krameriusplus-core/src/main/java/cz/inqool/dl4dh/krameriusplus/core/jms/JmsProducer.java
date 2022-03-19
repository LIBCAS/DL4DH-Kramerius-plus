package cz.inqool.dl4dh.krameriusplus.core.jms;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JmsProducer {

    private JmsTemplate jmsTemplate;

    private ActiveMQQueue enrichingQueue;

    private ActiveMQQueue exportingQueue;

    public void sendEnrichMessage(String publicationId){
        try{
            log.info("Attempting Send message : "+ publicationId);
            jmsTemplate.convertAndSend(enrichingQueue, publicationId);
        } catch(Exception e) {
            log.error("Received Exception during send Message: ", e);
        }
    }

    public void sendExportMessage(ExportMessage exportMessage){
        try{
            log.info("Attempting Send message : "+ exportMessage.toString());
            jmsTemplate.convertAndSend(exportingQueue, exportMessage);
        } catch(Exception e) {
            log.error("Received Exception during send Message: ", e);
        }
    }

    @Autowired
    public void setEnrichingQueue(@Value("${active-mq.queues.enriching-queue}") String queueName) {
        this.enrichingQueue = new ActiveMQQueue(queueName);
    }

    @Autowired
    public void setExportingQueue(@Value("${active-mq.queues.exporting-queue}") String queueName) {
        this.exportingQueue = new ActiveMQQueue(queueName);
    }

    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
}
