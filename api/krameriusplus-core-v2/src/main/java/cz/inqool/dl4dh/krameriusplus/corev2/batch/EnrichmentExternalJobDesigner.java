package cz.inqool.dl4dh.krameriusplus.corev2.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.KrameriusJobTypeName.ENRICHMENT_EXTERNAL;

@Component
public class EnrichmentExternalJobDesigner extends AbstractJobDesigner {

    private Step enrichPublicationModsStep;

    private Step enrichPagesAltoStep;

    @Override
    public String getJobName() {
        return ENRICHMENT_EXTERNAL;
    }

    @Bean(ENRICHMENT_EXTERNAL)
    @Override
    public Job build() {
        return getJobBuilder().start(enrichPublicationModsStep)
                .next(enrichPagesAltoStep)
                .build();
    }

    @Autowired
    public void setEnrichPagesAltoMasterStep(@Qualifier(ENRICH_PAGES_ALTO_MASTER) Step enrichPagesAltoStep) {
        this.enrichPagesAltoStep = enrichPagesAltoStep;
    }

    @Autowired
    public void setEnrichPublicationModsStep(@Qualifier(ENRICH_PUBLICATION_MODS) Step enrichPublicationModsStep) {
        this.enrichPublicationModsStep = enrichPublicationModsStep;
    }
}
