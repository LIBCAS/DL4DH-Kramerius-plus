package cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.enriching.enrich_external.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.enriching.common.CommonJobConfig;
import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.enriching.common.JobStep.*;


@Configuration
public class EnrichExternalJobConfig extends CommonJobConfig {

    @Bean
    public Job enrichExternalJob() {
        return jobBuilderFactory.get(KrameriusJob.ENRICHMENT_EXTERNAL.name())
                .listener(jobListener)
                .start(steps.get(DOWNLOAD_PAGES_ALTO))
                .next(steps.get(ENRICH_PAGES_UD_PIPE))
                .next(steps.get(ENRICH_PAGES_NAME_TAG))
                .next(steps.get(ENRICH_PAGES_ALTO))
                .next(steps.get(CLEAN_UP_PAGES))
                .build();
    }
}
