package cz.inqool.dl4dh.krameriusplus.service.scheduler;

import cz.inqool.dl4dh.krameriusplus.domain.dao.repo.EnrichmentTaskRepository;
import cz.inqool.dl4dh.krameriusplus.domain.entity.scheduling.EnrichmentTask;
import cz.inqool.dl4dh.krameriusplus.domain.exception.SchedulingException;
import cz.inqool.dl4dh.krameriusplus.service.filler.FillerService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.domain.entity.scheduling.EnrichmentTask.State.FAILED;
import static cz.inqool.dl4dh.krameriusplus.domain.exception.ExceptionUtils.isFalse;
import static cz.inqool.dl4dh.krameriusplus.domain.exception.ExceptionUtils.isTrue;
import static cz.inqool.dl4dh.krameriusplus.domain.exception.SchedulingException.ErrorCode.ALREADY_ENRICHED;
import static cz.inqool.dl4dh.krameriusplus.domain.exception.SchedulingException.ErrorCode.TASK_ALREADY_RUNNING;

/**
 * @author Norbert Bodnar
 */
@Service
@Slf4j
public class SchedulerService {

    private final FillerService fillerService;

    private final EnrichmentTaskRepository enrichmentTaskRepository;

    @Getter
    private static final Map<String, EnrichmentTask> tasks = new HashMap<>();

    @Autowired
    public SchedulerService(FillerService fillerService, EnrichmentTaskRepository enrichmentTaskRepository) {
        this.fillerService = fillerService;
        this.enrichmentTaskRepository = enrichmentTaskRepository;
    }

    public EnrichmentTask schedule(String publicationId, boolean overrideExisting) {
        EnrichmentTask existingTask = tasks.get(publicationId);

        isTrue(existingTask == null, () -> new SchedulingException(TASK_ALREADY_RUNNING,
                        "There is already a running task for a publication with this ID"));

        List<EnrichmentTask> enrichmentTasks = enrichmentTaskRepository.findEnrichmentTaskByPublicationId(publicationId);

        if (isAnySuccessful(enrichmentTasks)) {
            isTrue(overrideExisting, () -> new SchedulingException(ALREADY_ENRICHED,
                    "Publication with this ID was already enriched, " +
                            "to override existing enrichment, repeat request with 'override=true' param."));
            log.warn("Publication with PID: " + publicationId + " was already enriched, overriding");
        }

        EnrichmentTask task = new EnrichmentTask(publicationId);
        enrichmentTaskRepository.save(task);

        tasks.put(publicationId, task);

        fillerService.enrichPublication(publicationId, task);

        return task;
    }

    public List<EnrichmentTask> getFinishedTasks() {
        return enrichmentTaskRepository.findEnrichmentTaskByState(EnrichmentTask.State.SUCCESSFUL);
    }

    public static EnrichmentTask getTask(String pid) {
        EnrichmentTask result = tasks.get(pid);

        return result == null ? new EnrichmentTask(pid) : result;
    }

    public static void removeTask(String publicationId) {
        tasks.remove(publicationId);
    }

    private boolean isAnySuccessful(List<EnrichmentTask> enrichmentTasks) {
        return enrichmentTasks.stream().anyMatch(task -> task.getState() == EnrichmentTask.State.SUCCESSFUL);
    }
}
