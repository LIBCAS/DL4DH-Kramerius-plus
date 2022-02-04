package cz.inqool.dl4dh.krameriusplus.service.enricher;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.scheduling.EnrichmentTask;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.CompletePageEnricher;
import cz.inqool.dl4dh.krameriusplus.service.enricher.publication.PublicationEnricher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Component
@Slf4j
public class Enricher {

    private final PublicationEnricher publicationEnricher;
    private final CompletePageEnricher completePageEnricher;

    @Autowired
    public Enricher(PublicationEnricher publicationEnricher,
                    CompletePageEnricher completePageEnricher) {
        this.publicationEnricher = publicationEnricher;
        this.completePageEnricher = completePageEnricher;
    }

    public void enrich(Publication publication) {
        publicationEnricher.enrich(publication);
    }

    public void enrich(List<Page> pages, EnrichmentTask task) {
        completePageEnricher.enrich(pages, task);
    }

    private void fillParadata(Publication publication) {
        new ParadataFiller(publication).fill();
    }
}
