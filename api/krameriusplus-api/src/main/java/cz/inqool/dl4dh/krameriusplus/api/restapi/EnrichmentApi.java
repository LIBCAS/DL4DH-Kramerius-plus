package cz.inqool.dl4dh.krameriusplus.api.restapi;

import cz.inqool.dl4dh.krameriusplus.api.dto.PublicationContainerDto;
import cz.inqool.dl4dh.krameriusplus.core.jms.EnrichMessage;
import cz.inqool.dl4dh.krameriusplus.core.jms.JmsProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping("/api/enrich")
public class EnrichmentApi {

    private final JmsProducer jmsProducer;

    @Autowired
    public EnrichmentApi(JmsProducer jmsProducer) {
        this.jmsProducer = jmsProducer;
    }

    @PostMapping()
    public void enrich(@RequestBody PublicationContainerDto publicationsDto) {
        for (String publicationId : publicationsDto.getPublications()) {
            jmsProducer.sendEnrichMessage(new EnrichMessage(publicationId, Date.from(Instant.now())));
        }
    }
}
