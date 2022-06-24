package cz.inqool.dl4dh.krameriusplus.api.rest;

import cz.inqool.dl4dh.krameriusplus.service.jms.JmsProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestApi {

    private final JmsProducer jmsProducer;

    @Autowired
    public TestApi(JmsProducer jmsProducer) {
        this.jmsProducer = jmsProducer;
    }

    @GetMapping("/test")
    public void test(@RequestParam("numberOfMessages") int numberOfMessages) {
        for (int i = 0; i < numberOfMessages; i++) {
            jmsProducer.sendMessageConcurrencyTest(i);
        }
    }
}
