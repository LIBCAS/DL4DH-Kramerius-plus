package cz.inqool.dl4dh.krameriusplus.corev2.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.KrameriusJobTypeName.ENRICHMENT_EXTERNAL;
import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.ENRICH_ALTO_STEP;
import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.ENRICH_MODS_STEP;

@Configuration
public class EnrichmentExternalJobDesigner extends AbstractJobDesigner {

    private Step enrichModsStep;

    private Step enrichAltoStep;

    @Override
    public String getJobName() {
        return ENRICHMENT_EXTERNAL;
    }

    @Bean(ENRICHMENT_EXTERNAL)
    @Override
    public Job build() {
        return getJobBuilder()
                .start(enrichModsStep)
                .next(enrichAltoStep)
                .build();
    }

    @Autowired
    public void setEnrichPagesAltoMasterStep(@Qualifier(ENRICH_ALTO_STEP) Step enrichAltoStep) {
        this.enrichAltoStep = enrichAltoStep;
    }

    @Autowired
    public void setEnrichModsStep(@Qualifier(ENRICH_MODS_STEP) Step enrichModsStep) {
        this.enrichModsStep = enrichModsStep;
    }
}
