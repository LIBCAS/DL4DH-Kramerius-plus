package cz.inqool.dl4dh.krameriusplus.core.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.KrameriusJobTypeName.ENRICHMENT_NDK;
import static cz.inqool.dl4dh.krameriusplus.core.batch.step.KrameriusStep.ENRICH_NDK_STEP;

@Configuration
public class EnrichmentNdkJobDesigner extends AbstractJobDesigner {

    private Step enrichNdkStep;

    @Override
    public String getJobName() {
        return ENRICHMENT_NDK;
    }

    @Bean(ENRICHMENT_NDK)
    @Override
    public Job build() {
        return getJobBuilder()
                .start(enrichNdkStep)
                .build();
    }

    @Autowired
    public void setEnrichNdkStep(@Qualifier(ENRICH_NDK_STEP) Step enrichNdkStep) {
        this.enrichNdkStep = enrichNdkStep;
    }
}
