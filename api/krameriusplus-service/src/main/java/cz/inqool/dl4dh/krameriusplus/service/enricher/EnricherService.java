package cz.inqool.dl4dh.krameriusplus.service.enricher;

import cz.inqool.dl4dh.krameriusplus.domain.entity.PagesAware;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.paradata.NameTagParadata;
import cz.inqool.dl4dh.krameriusplus.domain.entity.paradata.UDPipeParadata;
import cz.inqool.dl4dh.krameriusplus.domain.entity.scheduling.EnrichmentTask;
import cz.inqool.dl4dh.krameriusplus.metadata.AltoWrapper;
import cz.inqool.dl4dh.krameriusplus.metadata.ModsWrapper;
import cz.inqool.dl4dh.krameriusplus.domain.entity.paradata.OCRParadata;
import cz.inqool.dl4dh.krameriusplus.service.enricher.tei.TeiConnector;
import cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider.StreamProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Norbert Bodnar
 */
@Service
@Slf4j
public class EnricherService {

    private final String PLAIN_TEXT_SOURCE;

    private final UDPipeService udPipeService;

    private final NameTagService nameTagService;

    private final StreamProvider streamProvider;

    private final TeiConnector teiConnector;

    @Autowired
    public EnricherService(UDPipeService udPipeService, NameTagService nameTagService, StreamProvider streamProvider,
                           @Value("${enrichment.source:OCR}") String plainTextSource, TeiConnector teiConnector) {
        this.udPipeService = udPipeService;
        this.nameTagService = nameTagService;
        this.streamProvider = streamProvider;
        this.PLAIN_TEXT_SOURCE = plainTextSource;
        this.teiConnector = teiConnector;
    }

    public void enrichPublication(Publication publication, EnrichmentTask task) {
        try {
            enrichPublicationChildren(publication, task);
            enrichPublicationWithMods(publication);
            enrichPublicationWithTeiHeader(publication);

            if (publication instanceof PagesAware) {
                enrichPages(((PagesAware) publication), task);
            }
        } catch (Exception e) {
            log.error("Error enriching publication", e);
        }
    }

    private void enrichPublicationWithTeiHeader(Publication publication) {
        publication.setTeiHeader(teiConnector.convertToTeiHeader(publication));
    }

    public void enrichPages(PagesAware publication, EnrichmentTask task) {
        int done = 1;
        int total = publication.getPages().size();
        task.setTotalPages(total);

        OCRParadata ocrParadata = null;
        UDPipeParadata udPipeParadata = null;
        NameTagParadata nameTagParadata = null;

        for (Page page : publication.getPages()) {
            task.setProcessingPage(done);
            enrichPage(page);

            if (ocrParadata == null || (page.getOcrParadata() != null && !ocrParadata.equals(page.getOcrParadata()))) {
                ocrParadata = page.getOcrParadata();
                publication.setOcrParadata(ocrParadata);
            }

            if (udPipeParadata == null || (page.getUdPipeParadata() != null && !udPipeParadata.equals(page.getUdPipeParadata()))) {
                udPipeParadata = page.getUdPipeParadata();
                publication.setUdPipeParadata(udPipeParadata);
            }

            if (nameTagParadata == null || (page.getNameTagParadata() != null && !nameTagParadata.equals(page.getNameTagParadata()))) {
                nameTagParadata = page.getNameTagParadata();
                publication.setNameTagParadata(nameTagParadata);
            }

            task.setPercentDone(calculatePercentDone(total, done++));
        }

        publication.setOcrParadata(ocrParadata);
    }

    private void enrichPublicationChildren(Publication publication, EnrichmentTask task) {
        for (Publication child : publication.getChildren()) {
            enrichPublication(child, task);
        }
    }

    private void enrichPage(Page page) {
        try {
            AltoWrapper altoWrapper = new AltoWrapper(streamProvider.getAlto(page.getId()));
            String content = getPageContent(page, altoWrapper);

            udPipeService.createTokens(page, content);
            nameTagService.processTokens(page);

            altoWrapper.enrichPage(page);
            page.setTeiBody(teiConnector.convertToTeiPage(page));
        } catch (Exception e) {
            log.error("Error enriching page with external services", e);
        }
    }

    private String getPageContent(Page page, AltoWrapper altoWrapper) {
        if (PLAIN_TEXT_SOURCE.equals("ALTO")) {
            return altoWrapper.extractPageContent();
        } else {
            return streamProvider.getTextOcr(page.getId());
        }
    }

    private void enrichPublicationWithMods(Publication publication) {
        ModsWrapper modsMetadataAdapter = new ModsWrapper(streamProvider.getMods(publication.getId()));
        publication.setModsMetadata(modsMetadataAdapter.getTransformedMods());
    }

    //todo: this has nothing to do here, but the whole enrichmentTask progress setting kinda sucks. We need a better
    //      system to keep track of the progress (maybe JMS ?)
    private double calculatePercentDone(int total, int done) {
        return Math.round((done / (double) total) * 10000) / (double) 100;
    }

}
