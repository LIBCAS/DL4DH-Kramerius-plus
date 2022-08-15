package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.ndk;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.EnrichmentJobConfig;
import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob.ENRICHMENT_NDK;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.ENRICH_PAGES_NDK;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.PREPARE_PAGES_NDK;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.PREPARE_PUBLICATION_NDK;

@Configuration
public class EnrichNdkJobConfig extends EnrichmentJobConfig {

    @Bean
    public Job enrichNdkJob() {
        return super.getJobBuilder()
                .next(stepContainer.getStep(PREPARE_PUBLICATION_NDK))
                .next(stepContainer.getStep(PREPARE_PAGES_NDK))
                .next(stepContainer.getStep(ENRICH_PAGES_NDK))
                .build();
    }

    @Override
    public String getJobName() {
        return ENRICHMENT_NDK.name();
    }
}
