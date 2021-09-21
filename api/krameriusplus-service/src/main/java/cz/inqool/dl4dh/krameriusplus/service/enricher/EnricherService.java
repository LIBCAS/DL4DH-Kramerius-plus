package cz.inqool.dl4dh.krameriusplus.service.enricher;

import cz.inqool.dl4dh.krameriusplus.domain.entity.PagesAware;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.scheduling.EnrichmentTask;
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
    private final PageEnricher pageEnricher;

    @Autowired
    public EnricherService(PublicationEnricher publicationEnricher, PageEnricher pageEnricher) {
        this.publicationEnricher = publicationEnricher;
        this.pageEnricher = pageEnricher;
    }

    public void enrich(Publication publication, EnrichmentTask task) {
        enrichPublicationChildren(publication, task);
        publicationEnricher.enrich(publication, task);

        if (publication instanceof PagesAware) {
            PagesAware publicationWithPages = (PagesAware) publication;
            pageEnricher.enrich(publicationWithPages.getPages(), task);
            fillParadata(publicationWithPages);
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
