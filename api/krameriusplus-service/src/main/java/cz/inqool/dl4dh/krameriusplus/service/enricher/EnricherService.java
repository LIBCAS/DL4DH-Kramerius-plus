package cz.inqool.dl4dh.krameriusplus.service.enricher;

import cz.inqool.dl4dh.krameriusplus.domain.entity.EnrichmentTask;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider.streams.StreamProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider.streams.StreamProvider.StreamType.TEXT_OCR;

/**
 * @author Norbert Bodnar
 */
@Service
@Slf4j
public class EnricherService {

    private final UDPipeService tokenizerService;

    private final NameTagService nameTagService;

    private final StreamProvider streamProvider;

    @Autowired
    public EnricherService(UDPipeService tokenizerService, NameTagService nameTagService, StreamProvider streamProvider) {
        this.tokenizerService = tokenizerService;
        this.nameTagService = nameTagService;
        this.streamProvider = streamProvider;
    }

    public void enrichPages(List<Page> pages, EnrichmentTask task) {
        int done = 1;
        int total = pages.size();
        task.setTotalPages(total);

        for (Page page : pages) {
            task.setProcessingPage(done);

            try {
                page.setTokens(tokenizerService.tokenize(streamProvider.getStreamAsString(page.getId(), TEXT_OCR)));
                page.setNameTagMetadata(nameTagService.processTokens(page.getTokens()));
            } catch (Exception e) {
                log.error("Error enriching data with external services", e);
            }

            task.setPercentDone(calculatePercentDone(total, done++));
        }
    }

    private double calculatePercentDone(int total, int done) {
        return Math.round((done / (double) total) * 10000) / (double) 100;
    }
}
