package cz.inqool.dl4dh.krameriusplus.api.restapi;

import cz.inqool.dl4dh.krameriusplus.core.batch.job.JobService;
import cz.inqool.dl4dh.krameriusplus.core.batch.job.dto.JobExecutionDto;
import cz.inqool.dl4dh.krameriusplus.core.batch.job.dto.JobInstanceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.EnrichingJobConfig.ENRICHING_JOB;

@RestController
@RequestMapping("/api/job")
public class JobApi {

    private final JobService jobService;

    @Autowired
    public JobApi(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/list")
    public List<String> listJobs() {
        return jobService.listJobs();
    }

    @GetMapping("/instance/enriching/list")
    public List<JobInstanceDto> listJobInstances() {
        return jobService.listJobInstances(ENRICHING_JOB);
    }

    @GetMapping("/instance/{instanceId}/executions")
    public List<JobExecutionDto> listInstanceRuns(@PathVariable("instanceId") Long instanceId) {
        return jobService.listJobExecutions(instanceId);
    }


}
