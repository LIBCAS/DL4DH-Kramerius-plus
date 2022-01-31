package cz.inqool.dl4dh.krameriusplus.service.enricher.publication;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.exception.EnrichingException;
import cz.inqool.dl4dh.krameriusplus.domain.exception.KrameriusException;
import cz.inqool.dl4dh.krameriusplus.metadata.ModsWrapper;
import cz.inqool.dl4dh.krameriusplus.service.enricher.page.mets.MetsFileFinder;
import cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider.StreamProvider;
import cz.inqool.dl4dh.krameriusplus.service.tei.TeiConnector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static cz.inqool.dl4dh.krameriusplus.domain.exception.EnrichingException.ErrorCode.KRAMERIUS_ERROR;

@Service
@Slf4j
public class PublicationEnricher {

    private final TeiConnector teiConnector;
    private final StreamProvider streamProvider;
    private final MetsFileFinder metsFileFinder;
    private String ndkPath;

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
            throw new EnrichingException(KRAMERIUS_ERROR, exception);
        }
    }

}
