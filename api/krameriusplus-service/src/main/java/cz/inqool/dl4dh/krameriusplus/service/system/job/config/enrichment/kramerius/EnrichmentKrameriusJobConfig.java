package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.EnrichmentBaseJobConfig;
import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobStep.*;

@Configuration
public class EnrichmentKrameriusJobConfig extends EnrichmentBaseJobConfig {

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
