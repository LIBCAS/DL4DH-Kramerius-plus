package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius;

import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.EnrichmentJobDesigner;
import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.DOWNLOAD_PUBLICATION;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.DOWNLOAD_PUBLICATION_CHILDREN;

@Configuration
public class EnrichmentKrameriusJobDesigner extends EnrichmentJobDesigner {

    @Bean
    public Job enrichingJob() {
        return super.getJobBuilder()
                .next(stepContainer.getStep(DOWNLOAD_PUBLICATION))
                .next(stepContainer.getStep(DOWNLOAD_PUBLICATION_CHILDREN))
                .build();
    }

    @Override
    public String getJobName() {
        return KrameriusJob.ENRICHMENT_KRAMERIUS.name();
    }
}
