package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.EnrichmentBaseJobConfig;
import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.KrameriusJob.ENRICHMENT_EXTERNAL;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.*;

@Configuration
public class EnrichmentExternalJobConfig extends EnrichmentBaseJobConfig {

    @Bean
    public Job enrichExternalJob() {
        return super.getJobBuilder()
                .start(stepContainer.getStep(ENRICHMENT_VALIDATION))
                .next(stepContainer.getStep(DOWNLOAD_PAGES_ALTO))
                .next(stepContainer.getStep(ENRICH_PAGES_UD_PIPE))
                .next(stepContainer.getStep(ENRICH_PAGES_NAME_TAG))
                .next(stepContainer.getStep(ENRICH_PAGES_ALTO))
                .next(stepContainer.getStep(CLEAN_UP_PAGES))
                .build();
    }

    @Override
    public String getJobName() {
        return ENRICHMENT_EXTERNAL.name();
    }
}
