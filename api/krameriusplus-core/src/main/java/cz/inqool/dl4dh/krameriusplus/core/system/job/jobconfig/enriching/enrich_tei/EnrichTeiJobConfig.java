package cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.enriching.enrich_tei;

import cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.common.EnrichmentJobValidator;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.enriching.EnrichmentBaseJobConfig;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.KrameriusJob.ENRICHMENT_TEI;
import static cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.common.JobStep.*;

@Configuration
public class EnrichTeiJobConfig extends EnrichmentBaseJobConfig {

    @Autowired
    public EnrichTeiJobConfig(EnrichmentJobValidator enrichmentJobValidator) {
        super(enrichmentJobValidator);
    }

    @Bean
    public Job enrichTeiJob() {
        return super.getJobBuilder()
                .start(steps.get(ENRICHMENT_VALIDATION))
                .next(steps.get(ENRICH_PUBLICATION_TEI))
                .next(steps.get(ENRICH_PAGES_TEI))
                .build();
    }

    @Override
    public String getJobName() {
        return ENRICHMENT_TEI.name();
    }
}
