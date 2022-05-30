package cz.inqool.dl4dh.krameriusplus.service.system.job.jobconfig.enriching.enrich_external;

import cz.inqool.dl4dh.krameriusplus.service.system.job.jobconfig.common.EnrichmentJobValidator;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobconfig.enriching.EnrichmentBaseJobConfig;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.jobconfig.KrameriusJob.ENRICHMENT_EXTERNAL;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.jobconfig.common.JobStep.*;


@Configuration
public class EnrichmentExternalJobConfig extends EnrichmentBaseJobConfig {

    @Autowired
    public EnrichmentExternalJobConfig(EnrichmentJobValidator enrichmentJobValidator) {
        super(enrichmentJobValidator);
    }

    @Bean
    public Job enrichExternalJob() {
        return super.getJobBuilder()
                .start(steps.get(ENRICHMENT_VALIDATION))
                .next(steps.get(DOWNLOAD_PAGES_ALTO))
                .next(steps.get(ENRICH_PAGES_UD_PIPE))
                .next(steps.get(ENRICH_PAGES_NAME_TAG))
                .next(steps.get(ENRICH_PAGES_ALTO))
                .next(steps.get(CLEAN_UP_PAGES))
                .build();
    }

    @Override
    public String getJobName() {
        return ENRICHMENT_EXTERNAL.name();
    }
}
