package cz.inqool.dl4dh.krameriusplus.api.restapi;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventRunner;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventService;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/job")
public class JobApi {

    private final JobEventService jobEventService;

    private final JobEventRunner jobEventRunner;

    @Autowired
    public JobApi(JobEventService jobEventService, JobEventRunner jobEventRunner) {
        this.jobEventService = jobEventService;
        this.jobEventRunner = jobEventRunner;
    }

    @GetMapping("/list/enriching")
    public QueryResults<JobEventDto> listEnrichingJobs(@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                                       @RequestParam(value = "publicationId", required = false) String publicationId) {
        return jobEventService.listEnrichingJobs(publicationId, page, pageSize);
    }

    @GetMapping("/list/exporting")
    public QueryResults<JobEventDto> listExportingJob(@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                      @RequestParam(value = "page", defaultValue = "0") int page,
                                                      @RequestParam(value = "publicationId", required = false) String publicationId) {
        return jobEventService.listExportingJobs(publicationId, page, pageSize);
    }

    @GetMapping("/{id}")
    public JobEventDto findJob(@PathVariable("id") String id) {
        return jobEventService.find(id);
    }

    @PostMapping("/{id}/restart")
    public JobEventDto restartJob(@PathVariable("id") String jobEventId) {
        return jobEventRunner.runJob(jobEventId);
    }
}
