package cz.inqool.dl4dh.krameriusplus.service.filler;

import cz.inqool.dl4dh.krameriusplus.domain.dao.EnrichmentTaskRepository;
import cz.inqool.dl4dh.krameriusplus.domain.dto.KrameriusMonographDto;
import cz.inqool.dl4dh.krameriusplus.domain.dto.KrameriusPublicationDto;
import cz.inqool.dl4dh.krameriusplus.domain.entity.EnrichmentTask;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Monograph;
import cz.inqool.dl4dh.krameriusplus.domain.service.DataProviderService;
import cz.inqool.dl4dh.krameriusplus.domain.service.EnricherService;
import cz.inqool.dl4dh.krameriusplus.domain.service.FillerService;
import cz.inqool.dl4dh.krameriusplus.service.scheduler.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * @author Norbert Bodnar
 */
//TODO: rename class
@Service
@Slf4j
public class FillerServiceImpl implements FillerService {

    private final DataProviderService dataProviderService;

    private final EnricherService enricherService;

    private final PublicationService publicationService;

    private final EnrichmentTaskRepository enrichmentTaskRepository;

    @Autowired
    public FillerServiceImpl(DataProviderService dataProviderService, EnricherService enricherService,
                             PublicationService publicationService, EnrichmentTaskRepository enrichmentTaskRepository) {
        this.dataProviderService = dataProviderService;
        this.enricherService = enricherService;
        this.publicationService = publicationService;
        this.enrichmentTaskRepository = enrichmentTaskRepository;
    }

    @Override
    public void enrichPublication(String pid) {
        log.info("Downloading pages");
        KrameriusPublicationDto publicationDto = dataProviderService.getPublication(pid);

        switch (publicationDto.getModel()) {
            case MONOGRAPH:
                processMonograph((KrameriusMonographDto) publicationDto);
                break;
            case PERIODICAL:
                throw new UnsupportedOperationException("Not implemented yet");
            default:
                throw new IllegalStateException("No such model");
        }
    }

    @Async
    @Override
    public void enrichPublicationAsync(String pid, EnrichmentTask task) {
        long start = System.currentTimeMillis();

        try {
            task.setState(EnrichmentTask.State.DOWNLOADING_PAGES);
            KrameriusPublicationDto publicationDto = dataProviderService.getPublication(pid);

            switch (publicationDto.getModel()) {
                case MONOGRAPH:
                    processMonograph((KrameriusMonographDto) publicationDto);
                    break;
                case PERIODICAL:
                    throw new UnsupportedOperationException("Not implemented yet");
                default:
                    throw new IllegalStateException("No such model");
            }

        } catch (Exception e) {
            log.error("Task wid pid: " + pid + " failed with error", e);
            SchedulerService.getTasks().get(pid).setState(EnrichmentTask.State.FAILED);
            return;
        }

        task.setTook(System.currentTimeMillis() - start);
        task.setFinished(Instant.now());
        task.setState(EnrichmentTask.State.SUCCESSFUL);
        enrichmentTaskRepository.save(task);

        SchedulerService.getTasks().remove(pid);
    }

    private void processMonograph(KrameriusMonographDto monographDto) {
        Monograph entity = enricherService.enrich(monographDto);
        publicationService.save(entity);
    }
}
