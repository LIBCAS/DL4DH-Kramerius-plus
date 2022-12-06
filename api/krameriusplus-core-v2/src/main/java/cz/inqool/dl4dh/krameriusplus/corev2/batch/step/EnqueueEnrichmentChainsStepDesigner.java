package cz.inqool.dl4dh.krameriusplus.corev2.batch.step;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.tasklet.EnrichmentChainEnqueuingTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.ENQUEUE_ENRICHMENT_CHAINS_STEP;

@Component
public class EnqueueEnrichmentChainsStepDesigner extends AbstractStepDesigner {

    private final EnrichmentChainEnqueuingTasklet enrichmentChainEnqueuingTasklet;

    @Autowired
    public EnqueueEnrichmentChainsStepDesigner(EnrichmentChainEnqueuingTasklet enrichmentChainEnqueuingTasklet) {
        this.enrichmentChainEnqueuingTasklet = enrichmentChainEnqueuingTasklet;
    }

    @Override
    protected String getStepName() {
        return ENQUEUE_ENRICHMENT_CHAINS_STEP;
    }

    @Override
    public Step build() {
        return getStepBuilder()
                .tasklet(enrichmentChainEnqueuingTasklet)
                .build();
    }
}
