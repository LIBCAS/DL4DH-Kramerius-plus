package cz.inqool.dl4dh.krameriusplus.api.restapi;

import cz.inqool.dl4dh.krameriusplus.api.dto.ScheduleMultipleDto;
import cz.inqool.dl4dh.krameriusplus.core.system.scheduling.EnrichmentTask;
import cz.inqool.dl4dh.krameriusplus.core.system.scheduling.SchedulerService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * @author Norbert Bodnar
 */
@RestController
@RequestMapping("api/scheduler")
public class SchedulerApi {

    private SchedulerService schedulerService;

    @Autowired
    public SchedulerApi(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @Operation(summary = "Schedule to enrich a publication with PID from path asynchronously.",
            description = "Add a publication with PID to a queue for enriching. In case of error, the task to enrich " +
                    "this publication stays in queue and the state is set to FAILED. If the publication with this PID " +
                    "is already in the queue and the state is not FAILED, this API call fails with IllegalArgumentException." +
                    "The enriching process is asynchronous and this method returns a running task without waiting.")
    @PostMapping(value = "/schedule/{pid}")
    public EnrichmentTask schedule(@PathVariable("pid") String pid,
                                   @RequestParam(value = "override", defaultValue = "false") boolean overrideExisting) {
        return schedulerService.schedule(pid, overrideExisting);
    }

    @Operation(summary = "Schedule to enrich multiple publications asynchronously.",
            description =  "The configuration for asynchronous thread pool is currently hardcoded for max 3 asynchronous " +
                    "threads at time. If more publications are in queue, they will be processed after a thread is released. ")
    @PostMapping(value = "/schedule")
    public void scheduleMultiple(@Valid @NotNull @RequestBody ScheduleMultipleDto dto,
                                 @RequestParam(value = "override", defaultValue = "false") boolean overrideExisting) {
        for (String pid : dto.getPublications()) {
            schedulerService.schedule(pid, overrideExisting);
        }
    }

    @Operation(summary = "Returns a list of tasks currently in queue.")
    @GetMapping(value = "/tasks/queue")
    public Collection<EnrichmentTask> getTasksRunning() {
        return SchedulerService.getTasks().values();
    }

    @Operation(summary = "Return a list of all finished tasks.")
    @GetMapping(value = "/tasks/finished")
    public Collection<EnrichmentTask> getTasksFinished() {
        return schedulerService.getFinishedTasks();
    }

    @Operation(summary = "Cancels a running task")
    @PostMapping(value = "/tasks/cancel/{id}")
    public void cancelTask(@PathVariable("id") String publicationId) {
        schedulerService.cancelTask(publicationId);
    }
}
