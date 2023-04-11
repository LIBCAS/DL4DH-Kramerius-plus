package cz.inqool.dl4dh.krameriusplus.core.batch;

import cz.inqool.dl4dh.krameriusplus.core.job.validator.EnrichmentJobValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.KrameriusJobTypeName.ENRICHMENT_EXTERNAL;
import static cz.inqool.dl4dh.krameriusplus.core.batch.step.KrameriusStep.ENRICH_ALTO_STEP;

@Configuration
public class EnrichmentExternalJobDesigner extends AbstractJobDesigner {

    private EnrichmentJobValidator enrichmentJobValidator;

    private Step enrichAltoStep;

    @Override
    public String getJobName() {
        return ENRICHMENT_EXTERNAL;
    }

    @Bean(ENRICHMENT_EXTERNAL)
    @Override
    public Job build() {
        return getJobBuilder()
                .validator(enrichmentJobValidator)
                .start(enrichAltoStep)
                .build();
    }

    @Autowired
    public void setEnrichAltoStep(@Qualifier(ENRICH_ALTO_STEP) Step enrichAltoStep) {
        this.enrichAltoStep = enrichAltoStep;
    }

    @Autowired
    public void setEnrichmentJobValidator(EnrichmentJobValidator enrichmentJobValidator) {
        this.enrichmentJobValidator = enrichmentJobValidator;
    }
}
