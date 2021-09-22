package cz.inqool.dl4dh.krameriusplus.api;

import cz.inqool.dl4dh.krameriusplus.domain.entity.scheduling.EnrichmentTask;
import cz.inqool.dl4dh.krameriusplus.dto.ScheduleMultipleDto;
import cz.inqool.dl4dh.krameriusplus.service.scheduler.SchedulerService;
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

    private final SchedulerService schedulerService;

    @Autowired
    public SchedulerApi(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @Operation(summary = "Schedule to enrich a publication with ID from path asynchronously.",
            description = "Add a publication with ID to a queue for enriching. In case of error, the state of the task" +
                    "is set to FAILED. If the publication with this ID is already in the queue or it has been enriched, " +
                    "this API call fails with EnrichingException. If you wish to override an already enriched publication," +
                    "you can do so by calling this endpoint with request parameter 'override=true'. " +
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
}
