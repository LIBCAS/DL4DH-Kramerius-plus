package cz.inqool.dl4dh.krameriusplus.service.scheduler;

import cz.inqool.dl4dh.krameriusplus.domain.dao.repo.EnrichmentTaskRepository;
import cz.inqool.dl4dh.krameriusplus.domain.entity.scheduling.EnrichmentTask;
import cz.inqool.dl4dh.krameriusplus.service.filler.FillerService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public EnrichmentTask schedule(String rootPublicationId, boolean overrideExisting) {
        EnrichmentTask existingTask = tasks.get(rootPublicationId);
        if (existingTask != null && existingTask.getState() != EnrichmentTask.State.FAILED) {
            throw new IllegalArgumentException("Task with this PID is already running");
        }

        List<EnrichmentTask> enrichmentTasks = enrichmentTaskRepository.findEnrichmentTaskByRootPublicationId(rootPublicationId);
        if (enrichmentTasks.stream().anyMatch(task -> task.getState() == EnrichmentTask.State.SUCCESSFUL)) {
            if (!overrideExisting) {
                log.error("Publication with PID: " + rootPublicationId + " was already enriched, to enrich again," +
                        " run with param override=true");
                return null;
            } else {
                log.warn("Publication with PID: " + rootPublicationId + " was already enriched, overriding");
            }
        }

        EnrichmentTask task = new EnrichmentTask(rootPublicationId);
        enrichmentTaskRepository.save(task);

        tasks.put(rootPublicationId, task);

        fillerService.enrichPublication(rootPublicationId, task);

        return task;
    }

    public List<EnrichmentTask> getFinishedTasks() {
        return enrichmentTaskRepository.findEnrichmentTaskByState(EnrichmentTask.State.SUCCESSFUL);
    }

    public static EnrichmentTask getTask(String pid) {
        EnrichmentTask result = tasks.get(pid);

        return result == null ? new EnrichmentTask("empty task") : result;
    }

    public static void removeTask(String publicationId) {
        tasks.remove(publicationId);
    }
}
