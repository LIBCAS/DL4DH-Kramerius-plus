package cz.inqool.dl4dh.krameriusplus.api.restapi;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventService;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/job")
public class JobApi {

    private final JobEventService jobEventService;

    @Autowired
    public JobApi(JobEventService jobEventService) {
        this.jobEventService = jobEventService;
    }

    @PostMapping("/list")
    public QueryResults<JobEventDto> listJobs(@RequestBody(required = false) Params params) {
        if (params == null) {
            params = new Params();
        }

        return jobEventService.list(params);
    }

    @GetMapping("/{id}")
    public JobEventDto findJob(@PathVariable("id") String id) {
        return jobEventService.find(id);
    }

    @PostMapping("/{id}/restart")
    public JobEventDto restartJob(@PathVariable("id") String jobEventId) {
        return jobEventService.runJob(jobEventId);
    }
}
