package cz.inqool.dl4dh.krameriusplus.service.system.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob.ENRICHMENT_EXTERNAL;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.ENRICH_PAGES_ALTO_MASTER;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.ENRICH_PUBLICATION_MODS;

@Configuration
public class EnrichmentExternalJobDesigner extends EnrichmentJobDesigner {

    private Step enrichPublicationModsStep;

    private Step enrichPagesAltoMasterStep;

    @Bean
    public Job enrichExternalJob() {
        return super.getJobBuilder()
                .next(enrichPublicationModsStep)
                .next(enrichPagesAltoMasterStep)
                .build();
    }

    @Override
    public String getJobName() {
        return ENRICHMENT_EXTERNAL.name();
    }

    @Autowired
    public void setEnrichPagesAltoMasterStep(@Qualifier(ENRICH_PAGES_ALTO_MASTER) Step enrichPagesAltoMasterStep) {
        this.enrichPagesAltoMasterStep = enrichPagesAltoMasterStep;
    }

    @Autowired
    public void setEnrichPublicationModsStep(@Qualifier(ENRICH_PUBLICATION_MODS) Step enrichPublicationModsStep) {
        this.enrichPublicationModsStep = enrichPublicationModsStep;
    }
}
