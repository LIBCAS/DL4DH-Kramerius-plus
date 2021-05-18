package cz.inqool.dl4dh.krameriusplus.service.enricher;

import cz.inqool.dl4dh.krameriusplus.dto.KrameriusPageDto;
import cz.inqool.dl4dh.krameriusplus.domain.entity.EnrichmentTask;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Service
@Slf4j
public class EnricherService {

    private final UDPipeService tokenizerService;

    private final NameTagService nameTagService;

    @Autowired
    public EnricherService(UDPipeService tokenizerService, NameTagService nameTagService) {
        this.tokenizerService = tokenizerService;
        this.nameTagService = nameTagService;
    }

    public List<Page> enrichPages(List<KrameriusPageDto> pageDtos, EnrichmentTask task) {
        List<Page> result = new ArrayList<>();

        Page page;
        int done = 1;
        int total = pageDtos.size();
        task.setTotalPages(total);

        for (KrameriusPageDto pageDto : pageDtos) {
            task.setProcessingPage(done);

            page = pageDto.toEntity();

            try {
                page.setTokens(tokenizerService.tokenize(pageDto.getTextOcr()));
                page.setNameTagMetadata(nameTagService.processTokens(page.getTokens()));
            } catch (Exception e) {
                log.error("Error enriching data with external services", e);
            }

            result.add(page);

            task.setPercentDone(calculatePercentDone(total, done++));
        }

        return result;
    }

    private double calculatePercentDone(int total, int done) {
        return Math.round((done / (double) total) * 10000) / (double) 100;
    }
}
