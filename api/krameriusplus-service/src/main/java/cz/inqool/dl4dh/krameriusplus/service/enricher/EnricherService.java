package cz.inqool.dl4dh.krameriusplus.service.enricher;

import cz.inqool.dl4dh.krameriusplus.domain.entity.PagesAware;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.scheduling.EnrichmentTask;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.CompletePageEnricher;
import cz.inqool.dl4dh.krameriusplus.service.enricher.publication.PublicationEnricher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Norbert Bodnar
 */
@Service
@Slf4j
public class EnricherService {

    private final PublicationEnricher publicationEnricher;
    private final CompletePageEnricher completePageEnricher;

    @Autowired
    public EnricherService(PublicationEnricher publicationEnricher, CompletePageEnricher completePageEnricher) {
        this.publicationEnricher = publicationEnricher;
        this.completePageEnricher = completePageEnricher;
    }

    public void enrich(Publication publication, EnrichmentTask task) {
        enrichPublicationChildren(publication, task);
        publicationEnricher.enrich(publication);

        if (publication instanceof PagesAware) {
            PagesAware publicationWithPages = (PagesAware) publication;
            completePageEnricher.enrich(publicationWithPages.getPages(), task);
            fillParadata(publicationWithPages); //TODO: should this be saved at publication level?
        }
    }

    private void enrichPublicationChildren(Publication publication, EnrichmentTask task) {
        for (Publication child : publication.getChildren()) {
            enrich(child, task);
        }
    }

    private void fillParadata(PagesAware publicationWithPages) {
        new ParadataFiller(publicationWithPages).fill();
    }
}
