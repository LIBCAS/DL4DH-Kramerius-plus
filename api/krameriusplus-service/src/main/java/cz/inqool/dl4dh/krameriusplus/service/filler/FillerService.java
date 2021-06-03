package cz.inqool.dl4dh.krameriusplus.service.filler;

import cz.inqool.dl4dh.krameriusplus.domain.dao.EnrichmentTaskRepository;
import cz.inqool.dl4dh.krameriusplus.domain.entity.DomainObject;
import cz.inqool.dl4dh.krameriusplus.domain.entity.EnrichmentTask;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.dataaccess.PublicationService;
import cz.inqool.dl4dh.krameriusplus.service.enricher.EnricherService;
import cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider.KrameriusProvider;
import cz.inqool.dl4dh.krameriusplus.service.scheduler.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;

/**
 * @author Norbert Bodnar
 */
@Service
@Slf4j
public class FillerService {

    private final KrameriusProvider krameriusProvider;

    private final EnricherService enricherService;

    private final PublicationService publicationService;

    private final EnrichmentTaskRepository enrichmentTaskRepository;

    @Autowired
    public FillerService(KrameriusProvider krameriusProvider, EnricherService enricherService,
                         PublicationService publicationService, EnrichmentTaskRepository enrichmentTaskRepository) {
        this.krameriusProvider = krameriusProvider;
        this.enricherService = enricherService;
        this.publicationService = publicationService;
        this.enrichmentTaskRepository = enrichmentTaskRepository;
    }

    @Async
    public void enrichPublication(String pid, EnrichmentTask task) {
        log.info("Downloading publication");
        DomainObject digitalObject = krameriusProvider.getDigitalObject(pid);

        try {
            if (digitalObject instanceof Publication) {
                enrichPublication((Publication) digitalObject, task);
            } else if (digitalObject instanceof Page) {
                enrichSinglePage((Page) digitalObject, task);
            } else {
                log.error("DigitalObject of class " + digitalObject.getClass().getSimpleName() + " not enrichable");
            }
        } catch (Exception e) {
            log.error("Task wid PID=" + pid + " failed with error ", e);
            SchedulerService.getTask(pid).setErrorMessage(e.getMessage());
            SchedulerService.getTask(pid).setState(EnrichmentTask.State.FAILED);
        }
    }

    private void enrichSinglePage(Page page, EnrichmentTask task) {
        long start = System.currentTimeMillis();

        enricherService.enrichPages(Collections.singletonList(page), task);

        task.setTook(System.currentTimeMillis() - start);
        task.setFinished(Instant.now());
        task.setState(EnrichmentTask.State.SUCCESSFUL);

        enrichmentTaskRepository.save(task);

        SchedulerService.removeTask(page.getId());
    }

    private void enrichPublication(Publication publication, EnrichmentTask task) {
        long start = System.currentTimeMillis();

        enricherService.enrichPages(publication.getPages(), task);

        task.setTook(System.currentTimeMillis() - start);
        task.setFinished(Instant.now());
        task.setState(EnrichmentTask.State.SUCCESSFUL);

        enrichmentTaskRepository.save(task);

        SchedulerService.removeTask(publication.getId());
    }

//    @Async
//    public void enrichPublicationAsync(String pid, EnrichmentTask task) {
//        long start = System.currentTimeMillis();
//
//        try {
//            PublicationDto publicationDto = krameriusProvider.getPublication(pid);
//
//            switch (publicationDto.getModel()) {
//                case MONOGRAPH:
//                    processMonograph((MonographDto) publicationDto);
//                    break;
//                case MONOGRAPH_UNIT:
//                    processMonographUnit((MonographUnitDto) publicationDto);
//                    break;
//                case PERIODICAL:
//                    throw new UnsupportedOperationException("Not implemented yet");
//                default:
//                    throw new IllegalStateException("No such model");
//            }
//        } catch (Exception e) {
//            log.error("Task wid PID=" + pid + " failed with error", e);
//            SchedulerService.getTask(pid).setErrorMessage(e.getMessage());
//            SchedulerService.getTask(pid).setState(EnrichmentTask.State.FAILED);
//            return;
//        }
//
//        task.setTook(System.currentTimeMillis() - start);
//        task.setFinished(Instant.now());
//        task.setState(EnrichmentTask.State.SUCCESSFUL);
//        enrichmentTaskRepository.save(task);
//
//        SchedulerService.removeTask(pid);
//    }


}
