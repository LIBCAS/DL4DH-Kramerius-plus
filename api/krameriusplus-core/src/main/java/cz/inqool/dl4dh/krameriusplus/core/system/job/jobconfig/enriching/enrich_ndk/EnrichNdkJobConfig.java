package cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.enriching.enrich_ndk;

import cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.common.EnrichmentJobValidator;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.enriching.EnrichmentBaseJobConfig;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.KrameriusJob.ENRICHMENT_NDK;
import static cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.common.JobStep.*;

@Configuration
public class EnrichNdkJobConfig extends EnrichmentBaseJobConfig {

    @Autowired
    public EnrichNdkJobConfig(EnrichmentJobValidator enrichmentJobValidator) {
        super(enrichmentJobValidator);
    }

    @Bean
    public Job enrichNdkJob() {
        return super.getJobBuilder()
                .start(steps.get(ENRICHMENT_VALIDATION))
                .next(steps.get(PREPARE_PUBLICATION_NDK))
                .next(steps.get(ENRICH_PUBLICATION_NDK))
                .next(steps.get(PREPARE_PAGES_NDK))
                .next(steps.get(ENRICH_PAGES_NDK))
                .build();
    }

    @Override
    public String getJobName() {
        return ENRICHMENT_NDK.name();
    }
}
