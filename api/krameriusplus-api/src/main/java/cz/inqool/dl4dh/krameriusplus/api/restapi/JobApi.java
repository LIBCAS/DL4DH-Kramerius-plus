package cz.inqool.dl4dh.krameriusplus.api.restapi;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventRunner;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventService;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/list")
    public QueryResults<JobEventDto> listJobs(@RequestBody(required = false) Params params) {
        if (params == null) {
            params = new Params();
        }

        return jobEventService.list(params);
    }

    @GetMapping("/list/enriching")
    public List<JobEventDto> listEnrichingJobs() {
        return jobEventService.listEnrichingJobs();
    }

    @GetMapping("/list/enriching/{id}")
    public List<JobEventDto> listEnrichingJobsByPublication(@PathVariable("id") String publicationId) {
        return jobEventService.listEnrichingJobs(publicationId);
    }

    @GetMapping("/list/exporting")
    public List<JobEventDto> listExportingJob() {
        return jobEventService.listExportingJobs();
    }

    @GetMapping("/list/exporting/{id}")
    public List<JobEventDto> listExportingJob(@PathVariable("id") String publicationId) {
        return jobEventService.listExportingJobs(publicationId);
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
