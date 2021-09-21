package cz.inqool.dl4dh.krameriusplus.service.filler;

import cz.inqool.dl4dh.krameriusplus.domain.dao.repo.EnrichmentTaskRepository;
import cz.inqool.dl4dh.krameriusplus.domain.entity.KrameriusObject;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.scheduling.EnrichmentTask;
import cz.inqool.dl4dh.krameriusplus.domain.exception.EnrichingException;
import cz.inqool.dl4dh.krameriusplus.service.dataaccess.PublicationService;
import cz.inqool.dl4dh.krameriusplus.service.enricher.EnricherService;
import cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider.KrameriusProvider;
import cz.inqool.dl4dh.krameriusplus.service.scheduler.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static cz.inqool.dl4dh.krameriusplus.domain.entity.scheduling.EnrichmentTask.State.ENRICHING;
import static cz.inqool.dl4dh.krameriusplus.domain.entity.scheduling.EnrichmentTask.State.FAILED;
import static cz.inqool.dl4dh.krameriusplus.domain.exception.EnrichingException.ErrorCode.TYPE_ERROR;

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
        try {
            log.info("Downloading publication");
            KrameriusObject digitalObject = krameriusProvider.getDigitalObject(pid);

            if (digitalObject instanceof Publication) {
                enrichPublication((Publication) digitalObject, task);
            } else if (digitalObject instanceof Page) {
                throw new EnrichingException(TYPE_ERROR, "Cannot enrich a single page");
            } else {
                throw new EnrichingException(TYPE_ERROR, "DigitalObject of class "
                        + digitalObject.getClass().getSimpleName() + " not enrichable");
            }
        } catch (Exception e) {
            log.error("Task wid PID=" + pid + " failed with error: " +  e.getMessage());
            task.setErrorMessage(e.getMessage());
            task.setState(FAILED);
            enrichmentTaskRepository.save(task);
            SchedulerService.removeTask(pid);
            throw e;
        }
    }

    private void enrichPublication(Publication publication, EnrichmentTask task) {
        log.info("Enriching publication: " + publication.getTitle());
        task.setState(ENRICHING);
        task.setPublicationTitle(publication.getTitle());
        task.setStarted(Instant.now());

        enricherService.enrich(publication, task);
        publicationService.save(publication);

        log.info("Saving publication: " + publication.getTitle());
        finishTask(task);
    }

    private void finishTask(EnrichmentTask task) {

        task.setFinished(Instant.now());
        task.setState(EnrichmentTask.State.SUCCESSFUL);

        enrichmentTaskRepository.save(task);

        log.info("Enrichment finished in " + task.getTook());
        SchedulerService.removeTask(task.getPublicationId());
    }
}
