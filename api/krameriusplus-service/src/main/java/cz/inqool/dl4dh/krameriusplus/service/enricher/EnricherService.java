package cz.inqool.dl4dh.krameriusplus.service.enricher;

import cz.inqool.dl4dh.krameriusplus.domain.dto.KrameriusMonographDto;
import cz.inqool.dl4dh.krameriusplus.domain.dto.KrameriusPageDto;
import cz.inqool.dl4dh.krameriusplus.domain.entity.EnrichmentTask;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Monograph;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Page;
import cz.inqool.dl4dh.krameriusplus.service.scheduler.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Monograph enrich(KrameriusMonographDto dto) {
        EnrichmentTask task = SchedulerService.getTasks().get(dto.getPid());

        log.info("Enriching monograph: PID=" + dto.getPid() + ", " + dto.getTitle());
        Monograph monograph = Monograph.from(dto);

        task.setState(EnrichmentTask.State.ENRICHING);

        Page page;
        int done = 1;
        int total = dto.getPages().size();
        task.setTotalPages(total);

        for (KrameriusPageDto pageDto : dto.getPages()) {
            task.setProcessingPage(done);

            page = tokenizerService.tokenizePage(pageDto);
            nameTagService.processPage(page);
            monograph.getPages().add(page);

            task.setPercentDone(calculatePercentDone(total, done++));
        }

        log.info("Enrichment of " + monograph.getTitle() + " finished");
        return monograph;
    }

    private double calculatePercentDone(int total, int done) {
        return Math.round((done / (double) total) * 10000) / (double) 100;
    }
}
