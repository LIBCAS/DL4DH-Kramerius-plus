package cz.inqool.dl4dh.krameriusplus.service.enricher;

import cz.inqool.dl4dh.krameriusplus.domain.entity.PagesAware;
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

    public void enrichPublication(Publication publication, EnrichmentTask task) {
        try {
            enrichPublicationChildren(publication, task);
            enrichPublicationWithMods(publication);

            if (publication instanceof PagesAware) {
                enrichPages(((PagesAware) publication).getPages(), task);
            }
        } catch (Exception e) {
            log.error("Error enriching publication", e);
        }
    }

    public void enrichPages(List<Page> pages, EnrichmentTask task) {
        int done = 1;
        int total = pages.size();
        task.setTotalPages(total);

        for (Page page : pages) {
            task.setProcessingPage(done);
            enrichPage(page);
            task.setPercentDone(calculatePercentDone(total, done++));
        }
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

            page.setTokens(tokenizerService.tokenize(content));
            page.setNameTagMetadata(nameTagService.processTokens(page.getTokens()));
            altoWrapper.enrichPage(page);
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
