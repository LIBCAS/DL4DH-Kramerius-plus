package cz.inqool.dl4dh.krameriusplus.service.scheduler;

import cz.inqool.dl4dh.krameriusplus.domain.dao.EnrichmentTaskRepository;
import cz.inqool.dl4dh.krameriusplus.domain.entity.EnrichmentTask;
import cz.inqool.dl4dh.krameriusplus.domain.service.FillerService;
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

    private static final Map<String, EnrichmentTask> tasks = new HashMap<>();

    @Autowired
    public SchedulerService(FillerService fillerService, EnrichmentTaskRepository enrichmentTaskRepository) {
        this.fillerService = fillerService;
        this.enrichmentTaskRepository = enrichmentTaskRepository;
    }

    public EnrichmentTask schedule(String rootPublicationId) {
        EnrichmentTask existingTask = tasks.get(rootPublicationId);
        if (existingTask != null && existingTask.getState() != EnrichmentTask.State.FAILED) {
            throw new IllegalArgumentException("Task with this PID is already running");
        }

        List<EnrichmentTask> enrichmentTasks = enrichmentTaskRepository.findEnrichmentTaskByRootPublicationId(rootPublicationId);
        if (enrichmentTasks.stream().anyMatch(task -> task.getState() == EnrichmentTask.State.SUCCESSFUL)) {
            log.warn("Publication with PID: " + rootPublicationId + " was already enriched");
        }

        EnrichmentTask task = new EnrichmentTask(rootPublicationId);
        enrichmentTaskRepository.save(task);

        tasks.put(rootPublicationId, task);

        fillerService.enrichPublicationAsync(rootPublicationId, task);

        return task;
    }

    public List<EnrichmentTask> getFinishedTasks() {
        return enrichmentTaskRepository.findEnrichmentTaskByState(EnrichmentTask.State.SUCCESSFUL);
    }

    public static Map<String, EnrichmentTask> getTasks() {
        return tasks;
    }
}
