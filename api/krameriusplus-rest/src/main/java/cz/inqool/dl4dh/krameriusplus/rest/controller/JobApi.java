package cz.inqool.dl4dh.krameriusplus.rest.controller;

import cz.inqool.dl4dh.krameriusplus.api.batch.job.KrameriusJobInstanceDto;
import cz.inqool.dl4dh.krameriusplus.api.job.JobFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
