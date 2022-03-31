package cz.inqool.dl4dh.krameriusplus.api.restapi;

import cz.inqool.dl4dh.krameriusplus.api.dto.PublicationContainerDto;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.exception.SchedulingException;
import cz.inqool.dl4dh.krameriusplus.core.jms.EnrichMessage;
import cz.inqool.dl4dh.krameriusplus.core.jms.JmsProducer;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.domain.mongo.exception.SchedulingException.ErrorCode.ALREADY_ENRICHED;

@RestController
@RequestMapping("/api/enrich")
public class EnrichmentApi {

    private final JmsProducer jmsProducer;

    private final PublicationService publicationService;

    @Autowired
    public EnrichmentApi(JmsProducer jmsProducer, PublicationService publicationService) {
        this.jmsProducer = jmsProducer;
        this.publicationService = publicationService;
    }

    @PostMapping()
    public void enrich(@RequestBody PublicationContainerDto publicationsDto, @RequestParam(value = "override", defaultValue = "false") boolean override) {
        List<String> alreadyEnrichedPublications = new ArrayList<>();
        for (String publicationId : publicationsDto.getPublications()) {
            if (publicationService.exists(publicationId)) {
                alreadyEnrichedPublications.add(publicationId);
            }
        }

        if (!alreadyEnrichedPublications.isEmpty() && !override) {
            throw new SchedulingException(ALREADY_ENRICHED,
                    "Publications " + alreadyEnrichedPublications + " are already enriched, to override, repeat request with request parameter" +
                            " 'override=true'");
        }

        for (String publicationId : publicationsDto.getPublications()) {
            jmsProducer.sendEnrichMessage(new EnrichMessage(publicationId, Date.from(Instant.now())));
        }
    }
}
