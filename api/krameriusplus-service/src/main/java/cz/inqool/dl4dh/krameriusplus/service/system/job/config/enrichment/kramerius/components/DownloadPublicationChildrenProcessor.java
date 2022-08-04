package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.service.jms.JmsProducer;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.JobPlanService;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.JOB_EVENT_ID;

@Component
@StepScope
public class DownloadPublicationChildrenProcessor implements ItemProcessor<DigitalObject, Page> {

    private final JobPlanService jobPlanService;

    private final JmsProducer jmsProducer;

    private final String jobEventId;

    @Autowired
    public DownloadPublicationChildrenProcessor(JobPlanService jobPlanService,
                                                JmsProducer jmsProducer,
                                                @Value("#{jobParameters['" + JOB_EVENT_ID + "']}") String jobEventId) {
        this.jobPlanService = jobPlanService;
        this.jmsProducer = jmsProducer;
        this.jobEventId = jobEventId;
    }

    @Override
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

    private void createJobPlanForChild(String childId) {
        JobEvent eventToEnqueue = jobPlanService.createForChild(jobEventId, childId);
        jmsProducer.sendMessage(eventToEnqueue.getConfig().getKrameriusJob(), eventToEnqueue.getId());
    }
}
