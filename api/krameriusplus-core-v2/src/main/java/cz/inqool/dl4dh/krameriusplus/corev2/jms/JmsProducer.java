package cz.inqool.dl4dh.krameriusplus.corev2.jms;

import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JmsProducer implements JobEnqueueService {

    private JmsTemplate jmsTemplate;

    @Override
    public void enqueue(KrameriusJobInstance job) {
        log.info("Attempting Send message : " + job.getId());
        jmsTemplate.convertAndSend(job.getJobType().getQueue(), job.getId());
    }

    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
}
