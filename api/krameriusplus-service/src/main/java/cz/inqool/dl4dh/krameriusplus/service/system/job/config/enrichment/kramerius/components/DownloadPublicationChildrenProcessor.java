package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.enrichmentrequest.EnrichmentRequestService;
import cz.inqool.dl4dh.krameriusplus.service.system.enrichmentrequest.dto.EnrichmentRequestDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.JobPlanService;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto.JobPlanDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.JOB_EVENT_ID;

@Component
@StepScope
public class DownloadPublicationChildrenProcessor implements ItemProcessor<DigitalObject, Page> {

    private final JobPlanService jobPlanService;

    private final String jobEventId;

    private final EnrichmentRequestService enrichmentRequestService;

    @Autowired
    public DownloadPublicationChildrenProcessor(JobPlanService jobPlanService,
                                                @Value("#{jobParameters['" + JOB_EVENT_ID + "']}") String jobEventId,
                                                EnrichmentRequestService enrichmentRequestService) {
        this.jobPlanService = jobPlanService;
        this.jobEventId = jobEventId;
        this.enrichmentRequestService = enrichmentRequestService;
    }

    @Override
    @Transactional
    public Page process(@NonNull DigitalObject item) {
        if (item instanceof Page) {
            return (Page) item;
        } else if (item instanceof Publication) {
            createJobPlanForChild(item.getId());
            return null;
        } else {
            return null; // stops processing of item
        }
    }

    private void createJobPlanForChild(String publicationId) {
        JobPlanDto parentPlanDto = jobPlanService.findByJobEvent(jobEventId);
        EnrichmentRequestDto enrichmentRequest = enrichmentRequestService.findByPlan(parentPlanDto.getId());

        JobPlanDto childPlan = jobPlanService.createForChild(jobEventId, publicationId);
        enrichmentRequest.getJobPlans().add(childPlan);
        enrichmentRequestService.update(enrichmentRequest);

        jobPlanService.startExecution(childPlan);
    }
}
