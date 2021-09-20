package cz.inqool.dl4dh.krameriusplus.service.enricher;

import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.scheduling.EnrichmentTask;
import cz.inqool.dl4dh.krameriusplus.metadata.AltoWrapper;
import cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider.StreamProvider;
import cz.inqool.dl4dh.krameriusplus.service.tei.TeiConnector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Slf4j
public class PageEnricher {

    private final String PLAIN_TEXT_SOURCE;

    private final UDPipeService udPipeService;

    private final NameTagService nameTagService;

    private final StreamProvider streamProvider;

    private final TeiConnector teiConnector;

    @Autowired
    public PageEnricher(UDPipeService udPipeService, NameTagService nameTagService, StreamProvider streamProvider,
                           @Value("${enrichment.source:OCR}") String plainTextSource, TeiConnector teiConnector) {
        this.udPipeService = udPipeService;
        this.nameTagService = nameTagService;
        this.streamProvider = streamProvider;
        this.PLAIN_TEXT_SOURCE = plainTextSource;
        this.teiConnector = teiConnector;
    }

    public void enrich(Collection<Page> pages, EnrichmentTask task) {
        int done = 1;
        int total = pages.size();
        task.setTotalPages(total);

        for (Page page : pages) {
            task.setProcessingPage(done);
            enrichPage(page);
            task.setPercentDone(calculatePercentDone(total, done++));
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

    //todo: this has nothing to do here, but the whole enrichmentTask progress setting kinda sucks. We need a better
    //      system to keep track of the progress (maybe JMS ?)
    private double calculatePercentDone(int total, int done) {
        return Math.round((done / (double) total) * 10000) / (double) 100;
    }
}
