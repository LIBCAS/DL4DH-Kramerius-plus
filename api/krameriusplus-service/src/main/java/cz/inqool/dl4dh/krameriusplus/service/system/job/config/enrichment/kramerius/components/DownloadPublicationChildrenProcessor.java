package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.JobEventService;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto.JobEventCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto.JobEventDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.jobeventconfig.dto.enrichment.EnrichmentKrameriusJobConfigDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobParameterKey.JOB_EVENT_ID;

@Component
@StepScope
public class DownloadPublicationChildrenProcessor implements ItemProcessor<DigitalObject, Page> {

    private final JobEventService jobEventService;

    private final TransactionTemplate transactionTemplate;

    private final String jobEventId;

    @Autowired
    public DownloadPublicationChildrenProcessor(JobEventService jobEventService,
                                                TransactionTemplate transactionTemplate,
                                                @Value("#{jobParameters['" + JOB_EVENT_ID + "']}") String jobEventId) {
        this.jobEventService = jobEventService;
        this.transactionTemplate = transactionTemplate;
        this.jobEventId = jobEventId;
    }

    @Override
    public Page process(@NonNull DigitalObject item) {
        if (item instanceof Page) {
            return (Page) item;
        } else if (item instanceof Publication) {
            createJobForChild(item.getId());
            return null;
        } else {
            return null; // stops processing of item
        }
    }


    private void createJobForChild(String childId) {
        JobEventDto parent = new JobEventDto();
        parent.setId(jobEventId);

        JobEventCreateDto createDto = new JobEventCreateDto();
        createDto.setConfig(new EnrichmentKrameriusJobConfigDto());
        createDto.setPublicationId(childId);
        createDto.setParent(parent);

        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        JobEventDto jobEventDto = transactionTemplate.execute(t -> jobEventService.create(createDto));
        jobEventService.enqueueJob(jobEventDto.getId());
    }
}
