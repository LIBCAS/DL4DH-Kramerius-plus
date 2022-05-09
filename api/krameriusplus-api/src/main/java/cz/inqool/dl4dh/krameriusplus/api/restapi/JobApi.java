package cz.inqool.dl4dh.krameriusplus.api.restapi;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventRunner;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventService;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Job", description = "Úlohy")
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

    @Operation(summary = "List enriching jobs.")
    @GetMapping("/list/enriching")
    public QueryResults<JobEventDto> listEnrichingJobs(@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                                       @Schema(description = "Optional publicationId parameter. When provided, only enriching jobs for the given publication will be returned.")
                                                           @RequestParam(value = "publicationId", required = false) String publicationId) {
        return jobEventService.listEnrichingJobs(publicationId, page, pageSize);
    }

    @Operation(summary = "List exporting jobs.")
    @GetMapping("/list/exporting")
    public QueryResults<JobEventDto> listExportingJob(@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                      @RequestParam(value = "page", defaultValue = "0") int page,
                                                      @Schema(description = "Optional publicationId parameter. When provided, only enriching jobs for the given publication will be returned.")
                                                          @RequestParam(value = "publicationId", required = false) String publicationId) {
        return jobEventService.listExportingJobs(publicationId, page, pageSize);
    }

    @Operation(summary = "Get detail of given Job")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Job with given ID not found.")
    @GetMapping("/{id}")
    public JobEventDto findJob(@PathVariable("id") String id) {
        return jobEventService.find(id);
    }

    @Operation(summary = "Restart job with given ID. Job can be restarted only if the last execution of the given job ended with status FAILED.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "400", description = "Job with given ID could not be restarted.")
    @PostMapping("/{id}/restart")
    public JobEventDto restartJob(@PathVariable("id") String jobEventId) {
        return jobEventRunner.runJob(jobEventId); //TODO: send to queue
    }
}
