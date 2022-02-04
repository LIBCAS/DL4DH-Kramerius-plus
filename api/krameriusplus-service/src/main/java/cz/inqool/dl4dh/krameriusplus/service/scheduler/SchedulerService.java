package cz.inqool.dl4dh.krameriusplus.service.scheduler;

import cz.inqool.dl4dh.krameriusplus.domain.entity.scheduling.EnrichmentTask;
import cz.inqool.dl4dh.krameriusplus.domain.entity.scheduling.EnrichmentTaskStore;
import cz.inqool.dl4dh.krameriusplus.domain.exception.SchedulingException;
import cz.inqool.dl4dh.krameriusplus.domain.params.Params;
import cz.inqool.dl4dh.krameriusplus.domain.params.filter.EqFilter;
import cz.inqool.dl4dh.krameriusplus.domain.params.filter.OrFilter;
import cz.inqool.dl4dh.krameriusplus.domain.params.filter.Sorting;
import cz.inqool.dl4dh.krameriusplus.service.dataaccess.PublicationService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.domain.entity.scheduling.EnrichmentTask.State.*;
import static cz.inqool.dl4dh.krameriusplus.domain.exception.ExceptionUtils.isTrue;
import static cz.inqool.dl4dh.krameriusplus.domain.exception.SchedulingException.ErrorCode.ALREADY_ENRICHED;
import static cz.inqool.dl4dh.krameriusplus.domain.exception.SchedulingException.ErrorCode.TASK_ALREADY_RUNNING;
import static java.util.Arrays.asList;

/**
 * @author Norbert Bodnar
 */
@Service
@Slf4j
public class SchedulerService {

    private final PublicationService publicationService;

    private final EnrichmentTaskStore enrichmentTaskStore;

    @Getter
    private static final Map<String, EnrichmentTask> tasks = new HashMap<>();

    @Autowired
    public SchedulerService(PublicationService publicationService, EnrichmentTaskStore enrichmentTaskStore) {
        this.publicationService = publicationService;
        this.enrichmentTaskStore = enrichmentTaskStore;
    }

    public EnrichmentTask schedule(String publicationId, boolean overrideExisting) {
        EnrichmentTask existingTask = tasks.get(publicationId);

        isTrue(existingTask == null, () -> new SchedulingException(TASK_ALREADY_RUNNING,
                        "There is already a running task for a publication with this ID"));

        Params params = new Params();
        params.addFilters(new EqFilter("publicationId", publicationId));

        List<EnrichmentTask> enrichmentTasks = enrichmentTaskStore.listAll(params);

        if (isAnySuccessful(enrichmentTasks)) {
            isTrue(overrideExisting, () -> new SchedulingException(ALREADY_ENRICHED,
                    "Publication with this ID was already enriched, " +
                            "to override existing enrichment, repeat request with 'override=true' param."));
            log.warn("Publication with PID: " + publicationId + " was already enriched, overriding");
        }

        EnrichmentTask task = new EnrichmentTask(publicationId);
        enrichmentTaskStore.create(task);

        tasks.put(publicationId, task);

        task.setFuture(publicationService.enrichPublication(publicationId, task));

        return task;
    }

    public List<EnrichmentTask> getFinishedTasks() {
        Params params = new Params();
        params.addFilters(new OrFilter(asList(
                new EqFilter("state", SUCCESSFUL),
                new EqFilter("state", FAILED),
                new EqFilter("state", CANCELED))));
        params.getSorting().add(new Sorting("created", Sort.Direction.DESC));

        return enrichmentTaskStore.listAll(params);
    }

    public void cancelTask(String publicationId) {
        EnrichmentTask task = tasks.remove(publicationId);
        task.getFuture().cancel(true);
        task.setState(CANCELED);
        enrichmentTaskStore.save(task);
    }

    public static EnrichmentTask getTask(String pid) {
        EnrichmentTask result = tasks.get(pid);

        return result == null ? new EnrichmentTask(pid) : result;
    }

    public static void removeTask(String publicationId) {
        tasks.remove(publicationId);
    }

    private boolean isAnySuccessful(List<EnrichmentTask> enrichmentTasks) {
        return enrichmentTasks.stream().anyMatch(task -> task.getState() == SUCCESSFUL);
    }
}
