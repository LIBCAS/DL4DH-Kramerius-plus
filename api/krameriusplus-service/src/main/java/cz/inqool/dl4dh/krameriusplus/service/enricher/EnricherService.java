package cz.inqool.dl4dh.krameriusplus.service.enricher;

import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.scheduling.EnrichmentTask;
import cz.inqool.dl4dh.krameriusplus.metadata.AltoWrapper;
import cz.inqool.dl4dh.krameriusplus.metadata.ModsWrapper;
import cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider.StreamProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Service
@Slf4j
public class EnricherService {

    private final UDPipeService tokenizerService;

    private final NameTagService nameTagService;

    private final StreamProvider streamProvider;

    private final String PLAIN_TEXT_SOURCE;

    @Autowired
    public EnricherService(UDPipeService tokenizerService, NameTagService nameTagService, StreamProvider streamProvider,
                           @Value("${enrichment.source:OCR}") String plainTextSource) {
        this.tokenizerService = tokenizerService;
        this.nameTagService = nameTagService;
        this.streamProvider = streamProvider;
        this.PLAIN_TEXT_SOURCE = plainTextSource;
    }

    public void enrichPublication(Publication publication) {
        try {
            enrichPublicationChildren(publication);
            enrichPublicationWithMods(publication);
        } catch (Exception e) {
            log.error("Error enriching publication", e);
        }
    }

    public void enrichPages(List<Page> pages, EnrichmentTask task) {
        int done = 1;
        int total = pages.size();
        task.setTotalPages(total);

        for (Page page : pages) {
            done = enrichPage(task, done, total, page);
        }
    }

    private void enrichPublicationChildren(Publication publication) {
        for (Publication child : publication.getChildren()) {
            enrichPublication(child);
        }
    }

    private int enrichPage(EnrichmentTask task, int done, int total, Page page) {
        task.setProcessingPage(done);

        try {
            AltoWrapper altoWrapper = new AltoWrapper(streamProvider.getAlto(page.getId()));
            String content;
            if (PLAIN_TEXT_SOURCE.equals("ALTO")) {
                content = altoWrapper.extractPageContent();
            } else {
                content = streamProvider.getTextOcr(page.getId());
            }
            page.setTokens(tokenizerService.tokenize(content));
            page.setNameTagMetadata(nameTagService.processTokens(page.getTokens()));
            altoWrapper.enrichWithAlto(page);
        } catch (Exception e) {
            log.error("Error enriching page with external services", e);
        }

        task.setPercentDone(calculatePercentDone(total, done++));
        return done;
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
