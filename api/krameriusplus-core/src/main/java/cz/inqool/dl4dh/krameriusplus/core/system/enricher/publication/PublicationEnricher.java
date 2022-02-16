package cz.inqool.dl4dh.krameriusplus.core.system.enricher.publication;

import cz.inqool.dl4dh.krameriusplus.core.domain.exception.EnrichingException;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.KrameriusException;
import cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.kramerius.StreamProvider;
import cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.tei.TeiConnector;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.mets.MetsFileFinder;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.publication.metadata.ModsWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PublicationEnricher {

    private final TeiConnector teiConnector;
    private final StreamProvider streamProvider;
    private final MetsFileFinder metsFileFinder;

    @Autowired
    public PublicationEnricher(TeiConnector teiConnector, StreamProvider streamProvider, MetsFileFinder metsFileFinder) {
        this.teiConnector = teiConnector;
        this.streamProvider = streamProvider;
        this.metsFileFinder = metsFileFinder;
    }

    public void enrich(Publication publication) {
        enrichPublicationWithMods(publication);
        enrichPublicationWithTeiHeader(publication);
        metsFileFinder.setMainMetsPath(publication);
    }

    private void enrichPublicationWithTeiHeader(Publication publication) {
        publication.setTeiHeader(teiConnector.convertToTeiHeader(publication));
    }

    private void enrichPublicationWithMods(Publication publication) {
        try {
            ModsWrapper modsMetadataAdapter = new ModsWrapper(streamProvider.getMods(publication.getId()));
            publication.setModsMetadata(modsMetadataAdapter.getTransformedMods());
        } catch (KrameriusException exception) {
            throw new EnrichingException(EnrichingException.ErrorCode.KRAMERIUS_ERROR, exception);
        }
    }

}
