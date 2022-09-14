package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.tei;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.EnrichmentJobDesigner;
import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob.ENRICHMENT_TEI;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.ENRICH_PAGES_TEI;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.ENRICH_PUBLICATION_TEI;

@Configuration
public class EnrichTeiJobDesigner extends EnrichmentJobDesigner {

    @Bean
    public Job enrichTeiJob() {
        return super.getJobBuilder()
                .next(stepContainer.getStep(ENRICH_PUBLICATION_TEI))
                .next(stepContainer.getStep(ENRICH_PAGES_TEI))
                .build();
    }

    @Override
    public String getJobName() {
        return ENRICHMENT_TEI.name();
    }
}
