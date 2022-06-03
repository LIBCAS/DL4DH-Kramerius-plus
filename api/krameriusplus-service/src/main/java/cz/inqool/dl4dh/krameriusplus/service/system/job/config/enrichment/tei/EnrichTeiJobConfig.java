package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.tei;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.EnrichmentBaseJobConfig;
import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.KrameriusJob.ENRICHMENT_TEI;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.*;

@Configuration
public class EnrichTeiJobConfig extends EnrichmentBaseJobConfig {

    @Bean
    public Job enrichTeiJob() {
        return super.getJobBuilder()
                .start(stepContainer.getStep(ENRICHMENT_VALIDATION))
                .next(stepContainer.getStep(ENRICH_PUBLICATION_TEI))
                .next(stepContainer.getStep(ENRICH_PAGES_TEI))
                .build();
    }

    @Override
    public String getJobName() {
        return ENRICHMENT_TEI.name();
    }
}
