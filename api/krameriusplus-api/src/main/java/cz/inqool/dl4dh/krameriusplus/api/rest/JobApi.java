package cz.inqool.dl4dh.krameriusplus.api.rest;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventFilter;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobStatus;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.JobEventService;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto.JobEventDetailDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Set;

@Tag(name = "Job", description = "Úlohy")
@RestController
@RequestMapping("/api/jobs")
@Slf4j
public class JobApi {

    private final JobEventService jobEventService;

    @Autowired
    public JobApi(JobEventService jobEventService) {
        this.jobEventService = jobEventService;
    }

    @Operation(summary = "List enriching jobs.")
    @GetMapping("/list/enriching")
    public QueryResults<JobEventDto> listEnrichingJobs(@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                                       @Schema(description = "Optional publicationId parameter. When provided, only enriching jobs for the given publication will be returned.")
                                                           @RequestParam(value = "publicationId", required = false) String publicationId,
                                                       @Schema(description = "Optional krameriusJob value. When provided, only enriching jobs of this type will be returned.")
                                                           @RequestParam(value = "jobType", required = false) KrameriusJob krameriusJob,
                                                       @Schema(description = "Optional lastExecutionStatus value. When provided, only enriching jobs with this lastExecutionStatus" +
                                                               " value will be returned.")
                                                           @RequestParam(value = "lastExecutionStatus", required = false) JobStatus jobStatus,
                                                       @Schema(description = "Optional includeDeleted flag. If true, deleted instances will be included. Defaults to false.")
                                                           @RequestParam(value = "includeDeleted", required = false, defaultValue = "false") boolean includeDeleted) {
        return jobEventService.listEnrichingJobs(
                new JobEventFilter(publicationId,  krameriusJob == null ? Collections.emptySet() : Set.of(krameriusJob),
                        jobStatus, includeDeleted), page, pageSize);
    }

    @Operation(summary = "List exporting jobs.")
    @GetMapping("/list/exporting")
    public QueryResults<JobEventDto> listExportingJob(@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                            @RequestParam(value = "page", defaultValue = "0") int page,
                                                            @Schema(description = "Optional publicationId parameter. When provided, only exporting jobs for the given publication will be returned.")
                                                          @RequestParam(value = "publicationId", required = false) String publicationId,
                                                      @Schema(description = "Optional krameriusJob value. When provided, only exporting jobs of this type will be returned.")
                                                          @RequestParam(value = "jobType", required = false) KrameriusJob krameriusJob,
                                                      @Schema(description = "Optional lastExecutionStatus value. When provided, only exporting jobs with this lastExecutionStatus" +
                                                              " value will be returned.")
                                                          @RequestParam(value = "lastExecutionStatus", required = false) JobStatus jobStatus,
                                                      @Schema(description = "Optional includeDeleted flag. If true, deleted instances will be included. Defaults to false.")
                                                          @RequestParam(value = "includeDeleted", required = false, defaultValue = "false") boolean includeDeleted) {
        return jobEventService.listExportingJobs(
                new JobEventFilter(publicationId, krameriusJob == null ? Collections.emptySet() : Set.of(krameriusJob),
                        jobStatus, includeDeleted), page, pageSize);
    }

    @Operation(summary = "Get detail of given Job")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Job with given ID not found.")
    @GetMapping("/{id}")
    public JobEventDetailDto findJob(@PathVariable("id") String id) {
        return jobEventService.findDetailed(id);
    }

    @Operation(summary = "Restart job with given ID. Job can be restarted only if the last execution of the given job ended with status FAILED.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "400", description = "Job with given ID could not be restarted.")
    @PostMapping("/{id}/restart")
    public ResponseEntity<?> restartJob(@PathVariable("id") String jobEventId) {
        jobEventService.restart(jobEventId);
        jobEventService.update(jobEventService.find(jobEventId));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Stop job with given ID.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "400", description = "Job with given ID could not be stopped.")
    @PostMapping("/{id}/stop")
    public ResponseEntity<?> stopJob(@PathVariable("id") String jobEventId) {
        jobEventService.stop(jobEventId);
        return ResponseEntity.ok().build();
    }
}
