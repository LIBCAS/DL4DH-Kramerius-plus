package cz.inqool.dl4dh.krameriusplus.rest.controller;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus;
import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.api.batch.job.KrameriusJobInstanceDto;
import cz.inqool.dl4dh.krameriusplus.api.job.JobEventFilter;
import cz.inqool.dl4dh.krameriusplus.api.job.JobFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Set;

@Tag(name = "Job", description = "Úlohy")
@RestController
@RequestMapping("/api/jobs")
@Slf4j
public class JobApi {

    private final JobFacade facade;

    @Autowired
    public JobApi(JobFacade facade) {
        this.facade = facade;
    }

    @Operation(summary = "List enrichment jobs.")
    @GetMapping("/enrichment/list")
    public Result<KrameriusJobInstanceDto> listEnrichingJobs(@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                             @RequestParam(value = "page", defaultValue = "0") int page,
                                                             @Schema(description = "Optional publicationId parameter. When provided, only enriching jobs for the given publication will be returned.")
                                                           @RequestParam(value = "publicationId", required = false) String publicationId,
                                                             @Schema(description = "Optional krameriusJob value. When provided, only enriching jobs of this type will be returned.")
                                                           @RequestParam(value = "jobType", required = false) KrameriusJobType jobType,
                                                             @Schema(description = "Optional lastExecutionStatus value. When provided, only enriching jobs with this lastExecutionStatus" +
                                                               " value will be returned.")
                                                           @RequestParam(value = "lastExecutionStatus", required = false) ExecutionStatus executionStatus,
                                                             @Schema(description = "Optional includeDeleted flag. If true, deleted instances will be included. Defaults to false.")
                                                           @RequestParam(value = "includeDeleted", required = false, defaultValue = "false") boolean includeDeleted) {
        return facade.listEnrichmentJobs(
                new JobEventFilter(publicationId,  jobType == null ? Collections.emptySet() : Set.of(jobType),
                        executionStatus, includeDeleted), page, pageSize);
    }

    @Operation(summary = "List export jobs.")
    @GetMapping("/export/list")
    public Result<KrameriusJobInstanceDto> listExportingJob(@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                 @Schema(description = "Optional publicationId parameter. When provided, only exporting jobs for the given publication will be returned.")
                                                          @RequestParam(value = "publicationId", required = false) String publicationId,
                                                 @Schema(description = "Optional krameriusJob value. When provided, only exporting jobs of this type will be returned.")
                                                          @RequestParam(value = "jobType", required = false) KrameriusJobType jobType,
                                                 @Schema(description = "Optional lastExecutionStatus value. When provided, only exporting jobs with this lastExecutionStatus" +
                                                              " value will be returned.")
                                                          @RequestParam(value = "lastExecutionStatus", required = false) ExecutionStatus executionStatus,
                                                 @Schema(description = "Optional includeDeleted flag. If true, deleted instances will be included. Defaults to false.")
                                                          @RequestParam(value = "includeDeleted", required = false, defaultValue = "false") boolean includeDeleted) {
        return facade.listExportJobs(
                new JobEventFilter(publicationId, jobType == null ? Collections.emptySet() : Set.of(jobType),
                        executionStatus, includeDeleted), page, pageSize);
    }

    @Operation(summary = "Get detail of given Job")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Job with given ID not found.")
    @GetMapping("/{id}")
    public KrameriusJobInstanceDto findJob(@PathVariable("id") String id) {
        return facade.findJob(id);
    }

    @Operation(summary = "Restart job with given ID. Job can be restarted only if the last execution of the given job ended with status FAILED.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "400", description = "Job with given ID could not be restarted.")
    @PostMapping("/{id}/restart")
    public void restartJob(@PathVariable("id") String jobEventId) {
        facade.restartJob(jobEventId);
    }

    @Operation(summary = "Stop job with given ID.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "400", description = "Job with given ID could not be stopped.")
    @PostMapping("/{id}/stop")
    public void stopJob(@PathVariable("id") String jobEventId) {
        facade.stopJob(jobEventId);
    }
}
