package cz.inqool.dl4dh.krameriusplus.service.filler;

import cz.inqool.dl4dh.krameriusplus.domain.dao.EnrichmentTaskRepository;
import cz.inqool.dl4dh.krameriusplus.domain.entity.EnrichmentTask;
import cz.inqool.dl4dh.krameriusplus.domain.entity.monograph.Monograph;
import cz.inqool.dl4dh.krameriusplus.domain.entity.monograph.MonographUnit;
import cz.inqool.dl4dh.krameriusplus.dto.PublicationDto;
import cz.inqool.dl4dh.krameriusplus.dto.monograph.MonographDto;
import cz.inqool.dl4dh.krameriusplus.dto.monograph.MonographUnitDto;
import cz.inqool.dl4dh.krameriusplus.service.enricher.EnricherService;
import cz.inqool.dl4dh.krameriusplus.service.filler.kramerius.KrameriusDataProvider;
import cz.inqool.dl4dh.krameriusplus.service.scheduler.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;

/**
 * @author Norbert Bodnar
 */
@Service
@Slf4j
public class FillerService {

    private final KrameriusDataProvider dataProviderService;

    private final EnricherService enricherService;

    private final PublicationService publicationService;

    private final EnrichmentTaskRepository enrichmentTaskRepository;

    @Autowired
    public FillerService(KrameriusDataProvider dataProviderService, EnricherService enricherService,
                         PublicationService publicationService, EnrichmentTaskRepository enrichmentTaskRepository) {
        this.dataProviderService = dataProviderService;
        this.enricherService = enricherService;
        this.publicationService = publicationService;
        this.enrichmentTaskRepository = enrichmentTaskRepository;
    }

    public void enrichPublication(String pid) {
        log.info("Downloading pages");
        PublicationDto publicationDto = dataProviderService.getPublication(pid);

        switch (publicationDto.getModel()) {
            case MONOGRAPH:
                processMonograph((MonographDto) publicationDto);
                break;
            case PERIODICAL:
                throw new UnsupportedOperationException("Not implemented yet");
            default:
                throw new IllegalStateException("No such model");
        }
    }

    @Async
    public void enrichPublicationAsync(String pid, EnrichmentTask task) {
        long start = System.currentTimeMillis();

        try {
            PublicationDto publicationDto = dataProviderService.getPublication(pid);

            switch (publicationDto.getModel()) {
                case MONOGRAPH:
                    processMonograph((MonographDto) publicationDto);
                    break;
                case MONOGRAPH_UNIT:
                    processMonographUnit((MonographUnitDto) publicationDto);
                    break;
                case PERIODICAL:
                    throw new UnsupportedOperationException("Not implemented yet");
                default:
                    throw new IllegalStateException("No such model");
            }
        } catch (Exception e) {
            log.error("Task wid PID=" + pid + " failed with error", e);
            SchedulerService.getTask(pid).setErrorMessage(e.getMessage());
            SchedulerService.getTask(pid).setState(EnrichmentTask.State.FAILED);
            return;
        }

        task.setTook(System.currentTimeMillis() - start);
        task.setFinished(Instant.now());
        task.setState(EnrichmentTask.State.SUCCESSFUL);
        enrichmentTaskRepository.save(task);

        SchedulerService.removeTask(pid);
    }

    private void processMonograph(MonographDto monographDto) {
        //todo: divide into enriching and storing list of pages separately(so in case of monographunits thousands of
        // pages are not stored in memory)
        Monograph monograph = monographDto.toEntity();
        publicationService.save(monograph);

        EnrichmentTask task = SchedulerService.getTask(monographDto.getPid());

        log.info("Enriching monograph: PID=" + monograph.getPid() + ", " + monograph.getTitle());
        if (monographDto.getPages() != null) {
            monograph.setPages(enricherService.enrichPages(monographDto.getPages(), task));
            publicationService.save(monograph.getPages());
        } else {
            monograph.setMonographUnits(new ArrayList<>());
            MonographUnit monographUnit;
            for (MonographUnitDto monographUnitDto : monographDto.getMonographUnits()) {
                monographUnit = monographUnitDto.toEntity();
                publicationService.save(monographUnit);

                task.setTitle(task.getTitle() + " - [" + monographUnit.getPartNumber() + "]" + monographUnit.getPartTitle());
                monographUnit.setPages(enricherService.enrichPages(monographUnitDto.getPages(), task));
                publicationService.save(monographUnit.getPages());
            }
        }
        log.info("Enrichment of " + monograph.getTitle() + " finished");
    }

    private void processMonographUnit(MonographUnitDto monographUnitDto) {
        //todo: divide into enriching and storing list of pages separately(so in case of monographunits thousands of
        // pages are not stored in memory)
        MonographUnit monographUnit = monographUnitDto.toEntity();
        publicationService.save(monographUnit);

        EnrichmentTask task = SchedulerService.getTask(monographUnitDto.getPid());

        log.info("Enriching monographUnit: PID=" + monographUnit.getPid() + ", " + monographUnit.getTitle());

        monographUnit.setPages(enricherService.enrichPages(monographUnitDto.getPages(), task));
        publicationService.save(monographUnit.getPages());

        log.info("Enrichment of " + monographUnit.getTitle() + " finished");
    }
}
