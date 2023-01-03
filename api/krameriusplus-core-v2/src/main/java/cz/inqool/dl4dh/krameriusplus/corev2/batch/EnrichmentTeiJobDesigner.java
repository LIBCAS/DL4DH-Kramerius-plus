package cz.inqool.dl4dh.krameriusplus.corev2.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.KrameriusJobTypeName.ENRICHMENT_TEI;
import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.ENRICH_TEI_PAGES_STEP;
import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.ENRICH_TEI_PUBLICATION_STEP;

@Configuration
public class EnrichmentTeiJobDesigner extends AbstractJobDesigner {

    private Step enrichTeiPublicationStep;

    private Step enrichTeiPagesStep;

    @Override
    public String getJobName() {
        return ENRICHMENT_TEI;
    }

    @Bean(ENRICHMENT_TEI)
    @Override
    public Job build() {
        return getJobBuilder()
                .start(enrichTeiPublicationStep)
                .next(enrichTeiPagesStep)
                .build();
    }

    @Autowired
    public void setEnrichTeiPublicationStep(@Qualifier(ENRICH_TEI_PUBLICATION_STEP) Step enrichTeiPublicationStep) {
        this.enrichTeiPublicationStep = enrichTeiPublicationStep;
    }

    @Autowired
    public void setEnrichTeiPagesStep(@Qualifier(ENRICH_TEI_PAGES_STEP) Step enrichTeiPagesStep) {
        this.enrichTeiPagesStep = enrichTeiPagesStep;
    }
}
