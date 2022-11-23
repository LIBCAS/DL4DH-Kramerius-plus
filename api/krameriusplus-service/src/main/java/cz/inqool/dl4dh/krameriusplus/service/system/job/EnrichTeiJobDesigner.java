package cz.inqool.dl4dh.krameriusplus.service.system.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob.ENRICHMENT_TEI;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.ENRICH_PAGES_TEI_MASTER;

@Configuration
public class EnrichTeiJobDesigner extends EnrichmentJobDesigner {

    private Step enrichPagesTeiMasterStep;

    @Bean
    public Job enrichTeiJob() {
        return getJobBuilder()
                .next(enrichPagesTeiMasterStep)
                .build();
    }

    @Override
    public String getJobName() {
        return ENRICHMENT_TEI.name();
    }

    @Autowired
    public void setEnrichPagesTeiMasterStep(@Qualifier(ENRICH_PAGES_TEI_MASTER) Step enrichPagesTeiMasterStep) {
        this.enrichPagesTeiMasterStep = enrichPagesTeiMasterStep;
    }
}
