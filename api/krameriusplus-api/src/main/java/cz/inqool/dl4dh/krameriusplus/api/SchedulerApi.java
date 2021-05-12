package cz.inqool.dl4dh.krameriusplus.api;

import cz.inqool.dl4dh.krameriusplus.domain.entity.EnrichmentTask;
import cz.inqool.dl4dh.krameriusplus.dto.ScheduleMultipleDto;
import cz.inqool.dl4dh.krameriusplus.service.scheduler.SchedulerService;
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

    @PostMapping(value = "/schedule/{pid}")
    public EnrichmentTask schedule(@PathVariable("pid") String pid) {
        return schedulerService.schedule(pid);
    }

    @PostMapping(value = "/schedule")
    public void scheduleMultiple(@Valid @NotNull @RequestBody ScheduleMultipleDto dto) {
        for (String pid : dto.getPublications()) {
            schedulerService.schedule(pid);
        }
    }

    @GetMapping(value = "/tasks/running")
    public Collection<EnrichmentTask> getTasksRunning() {
        return SchedulerService.getTasks().values();
    }

    @GetMapping(value = "/tasks/finished")
    public Collection<EnrichmentTask> getTasksFinished() {
        return schedulerService.getFinishedTasks();
    }
}
