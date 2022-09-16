package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.EnrichmentJobDesigner;
import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob.ENRICHMENT_EXTERNAL;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.*;

@Configuration
public class EnrichmentExternalJobDesigner extends EnrichmentJobDesigner {

    @Bean
    public Job enrichExternalJob() {
        return super.getJobBuilder()
                .next(stepContainer.getStep(DOWNLOAD_PAGES_ALTO))
                .next(stepContainer.getStep(ENRICH_PAGES_UD_PIPE))
                .next(stepContainer.getStep(ENRICH_PAGES_NAME_TAG))
                .next(stepContainer.getStep(ENRICH_PAGES_ALTO))
                .next(stepContainer.getStep(ENRICH_PUBLICATION_MODS))
                .next(stepContainer.getStep(CLEAN_UP_PAGES))
                .build();
    }

    @Override
    public String getJobName() {
        return ENRICHMENT_EXTERNAL.name();
    }
}
