package cz.inqool.dl4dh.krameriusplus.api.restapi;

import cz.inqool.dl4dh.krameriusplus.api.dto.PublicationContainerDto;
import cz.inqool.dl4dh.krameriusplus.core.batch.job.JobService;
import cz.inqool.dl4dh.krameriusplus.core.batch.job.dto.JobExecutionDto;
import cz.inqool.dl4dh.krameriusplus.core.batch.job.dto.JobInstanceDto;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.jms.EnrichMessage;
import cz.inqool.dl4dh.krameriusplus.core.jms.ExportMessage;
import cz.inqool.dl4dh.krameriusplus.core.jms.JmsProducer;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublicationService;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.EnrichingJobConfig.ENRICHING_JOB;
import static cz.inqool.dl4dh.krameriusplus.core.batch.job.export.json.JsonExportingJobConfig.JSON_EXPORTING_JOB;

@RestController
@RequestMapping("/api/job")
public class JobApi {

    private final JobService jobService;

    private final JmsProducer jmsProducer;

    private final PublicationService publicationService;

    @Autowired
    public JobApi(JobService jobService, JmsProducer jmsProducer, PublicationService publicationService) {
        this.jobService = jobService;
        this.jmsProducer = jmsProducer;
        this.publicationService = publicationService;
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
        return jobService.listJobInstances(JSON_EXPORTING_JOB);
    }

    @GetMapping("/instance/{instanceId}/executions")
    public List<JobExecutionDto> listInstanceRuns(@PathVariable("instanceId") Long instanceId) {
        return jobService.listJobExecutions(instanceId);
    }

    @PostMapping("/instance/enrich")
    public void enrich(@RequestBody PublicationContainerDto publicationsDto) {
        for (String publicationId : publicationsDto.getPublications()) {
            jmsProducer.sendEnrichMessage(new EnrichMessage(publicationId, Date.from(Instant.now())));
        }
    }

    @PostMapping("/execution/{executionId}/restart")
    public void restartJob(@PathVariable("executionId") Long executionId) {
        jmsProducer.sendEnrichMessage(new EnrichMessage(executionId));
    }

    @PostMapping("/instance/export/{id}/{format}")
    public void export(@PathVariable("id") String publicationId,
                       @PathVariable("format") String stringFormat,
                       @RequestBody(required = false) Params params) {
        Publication publication = publicationService.find(publicationId);

        if (params == null) {
            params = new Params();
        }

        jmsProducer.sendExportMessage(
                new ExportMessage(publicationId, publication.getTitle(), params, ExportFormat.fromString(stringFormat)));
    }
}
