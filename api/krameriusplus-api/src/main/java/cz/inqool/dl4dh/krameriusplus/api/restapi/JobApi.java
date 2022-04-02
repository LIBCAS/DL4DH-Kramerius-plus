package cz.inqool.dl4dh.krameriusplus.api.restapi;

import cz.inqool.dl4dh.krameriusplus.core.batch.job.JobService;
import cz.inqool.dl4dh.krameriusplus.core.batch.job.dto.JobExecutionDto;
import cz.inqool.dl4dh.krameriusplus.core.batch.job.dto.JobInstanceDto;
import cz.inqool.dl4dh.krameriusplus.core.jms.EnrichMessage;
import cz.inqool.dl4dh.krameriusplus.core.jms.JmsProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.EnrichingJobConfig.ENRICHING_JOB;
import static cz.inqool.dl4dh.krameriusplus.core.batch.job.export.json.JsonExportingJobConfig.JSON_EXPORTING_JOB;
import static cz.inqool.dl4dh.krameriusplus.core.batch.job.export.tei.ExportingJobConfig.EXPORTING_JOB;

@RestController
@RequestMapping("/api/job")
public class JobApi {

    private final JobService jobService;

    private final JmsProducer jmsProducer;

    @Autowired
    public JobApi(JobService jobService, JmsProducer jmsProducer) {
        this.jobService = jobService;
        this.jmsProducer = jmsProducer;
    }

    @GetMapping("/list")
    public List<String> listJobs() {
        return jobService.listJobs();
    }

    @GetMapping("/instance/enriching/list")
    public List<JobInstanceDto> listEnrichingJobs() {
        return jobService.listJobInstances(ENRICHING_JOB);
    }

    @GetMapping("/instance/export/list")
    public List<JobInstanceDto> listExportingJobs() {
        List<JobInstanceDto> jobInstanceDtos = jobService.listJobInstances(JSON_EXPORTING_JOB);
        jobInstanceDtos.addAll(jobService.listJobInstances(EXPORTING_JOB));

        return jobInstanceDtos;
    }

    @GetMapping("/instance/{instanceId}/executions")
    public List<JobExecutionDto> listInstanceRuns(@PathVariable("instanceId") Long instanceId) {
        return jobService.listJobExecutions(instanceId);
    }

    @PostMapping("/execution/{executionId}/restart")
    public void restartJob(@PathVariable("executionId") Long executionId) {
        jmsProducer.sendEnrichMessage(new EnrichMessage(executionId));
    }
}
