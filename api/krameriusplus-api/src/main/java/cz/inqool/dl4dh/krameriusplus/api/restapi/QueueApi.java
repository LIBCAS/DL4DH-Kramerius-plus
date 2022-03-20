package cz.inqool.dl4dh.krameriusplus.api.restapi;

import cz.inqool.dl4dh.krameriusplus.api.dto.PublicationContainerDto;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.jms.ExportMessage;
import cz.inqool.dl4dh.krameriusplus.core.jms.JmsProducer;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublicationService;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/queue")
public class QueueApi {

    private final JmsProducer jmsProducer;

    private final PublicationService publicationService;

    @Autowired
    public QueueApi(JmsProducer jmsProducer, PublicationService publicationService) {
        this.jmsProducer = jmsProducer;
        this.publicationService = publicationService;
    }

    @PostMapping("/enrich")
    public void enrich(@RequestBody PublicationContainerDto publicationsDto) {
        for (String publicationId : publicationsDto.getPublications()) {
            jmsProducer.sendEnrichMessage(publicationId);
        }
    }

    @PostMapping("/{id}/{format}")
    public void export(@PathVariable("id") String publicationId,
                          @PathVariable("format") String stringFormat,
                          @RequestBody(required = false) Params params) {
        Publication publication = publicationService.find(publicationId);

        if (params == null) {
            params = new Params();
        }

        jmsProducer.sendExportMessage(
                new ExportMessage(publicationId, publication.getTitle(), params, ExportFormat.fromString(stringFormat)));
    }


}
