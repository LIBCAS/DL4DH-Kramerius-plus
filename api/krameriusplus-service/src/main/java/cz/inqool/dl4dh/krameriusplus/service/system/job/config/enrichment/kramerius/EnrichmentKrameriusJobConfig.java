package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius;

import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.EnrichmentJobConfig;
import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.*;

@Configuration
public class EnrichmentKrameriusJobConfig extends EnrichmentJobConfig {

    @Bean
    public Job enrichingJob() {
        return super.getJobBuilder()
                .start(stepContainer.getStep(ENRICHMENT_VALIDATION))
                .next(stepContainer.getStep(DOWNLOAD_PUBLICATION))
                .next(stepContainer.getStep(DOWNLOAD_PUBLICATION_CHILDREN))
                .build();
    }

    @Override
    public String getJobName() {
        return KrameriusJob.ENRICHMENT_KRAMERIUS.name();
    }
}
