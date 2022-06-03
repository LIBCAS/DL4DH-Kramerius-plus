package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.ndk;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.EnrichmentBaseJobConfig;
import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.KrameriusJob.ENRICHMENT_NDK;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.*;

@Configuration
public class EnrichNdkJobConfig extends EnrichmentBaseJobConfig {

    @Bean
    public Job enrichNdkJob() {
        return super.getJobBuilder()
                .start(stepContainer.getStep(ENRICHMENT_VALIDATION))
                .next(stepContainer.getStep(PREPARE_PUBLICATION_NDK))
                .next(stepContainer.getStep(ENRICH_PUBLICATION_NDK))
                .next(stepContainer.getStep(PREPARE_PAGES_NDK))
                .next(stepContainer.getStep(ENRICH_PAGES_NDK))
                .build();
    }

    @Override
    public String getJobName() {
        return ENRICHMENT_NDK.name();
    }
}
