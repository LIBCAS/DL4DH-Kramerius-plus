package cz.inqool.dl4dh.krameriusplus.service.enricher;

import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.scheduling.EnrichmentTask;
import cz.inqool.dl4dh.krameriusplus.metadata.ModsWrapper;
import cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider.StreamProvider;
import cz.inqool.dl4dh.krameriusplus.service.tei.TeiConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PublicationEnricher {

    private final TeiConnector teiConnector;
    private final StreamProvider streamProvider;

    @Autowired
    public PublicationEnricher(@Qualifier("default") TeiConnector teiConnector, StreamProvider streamProvider) {
        this.teiConnector = teiConnector;
        this.streamProvider = streamProvider;
    }

    public void enrich(Publication publication, EnrichmentTask task) {
        enrichPublicationChildren(publication, task);
        enrichPublicationWithMods(publication);
        enrichPublicationWithTeiHeader(publication);
    }

    private void enrichPublicationWithTeiHeader(Publication publication) {
        publication.setTeiHeader(teiConnector.convertToTeiHeader(publication));
    }

    private void enrichPublicationChildren(Publication publication, EnrichmentTask task) {
        for (Publication child : publication.getChildren()) {
            enrich(child, task);
        }
    }

    private void enrichPublicationWithMods(Publication publication) {
        ModsWrapper modsMetadataAdapter = new ModsWrapper(streamProvider.getMods(publication.getId()));
        publication.setModsMetadata(modsMetadataAdapter.getTransformedMods());
    }
}
