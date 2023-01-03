package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.designer;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.reader.EnrichmentRequestReader;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.writer.EnrichmentChainWriter;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.chain.EnrichmentChain;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.CREATE_ENRICHMENT_CHAINS_STEP;
import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor.ItemProcessorsConfig.KrameriusProcessor.CREATE_CHAINS_COMPOSITE_PROCESSOR;

@Component
public class CreateEnrichmentChainsStepDesigner extends AbstractStepDesigner {

    private final EnrichmentRequestReader enrichmentRequestReader;

    private final CompositeItemProcessor<String, List<EnrichmentChain>> createChainsCompositeProcessor;

    private final EnrichmentChainWriter enrichmentChainWriter;

    @Autowired
    public CreateEnrichmentChainsStepDesigner(EnrichmentRequestReader enrichmentRequestReader,
                                              @Qualifier(CREATE_CHAINS_COMPOSITE_PROCESSOR)
                                              CompositeItemProcessor<String, List<EnrichmentChain>> createChainsCompositeProcessor,
                                              EnrichmentChainWriter enrichmentChainWriter) {
        this.enrichmentRequestReader = enrichmentRequestReader;
        this.createChainsCompositeProcessor = createChainsCompositeProcessor;
        this.enrichmentChainWriter = enrichmentChainWriter;
    }

    @Override
    protected String getStepName() {
        return CREATE_ENRICHMENT_CHAINS_STEP;
    }

    @Bean(CREATE_ENRICHMENT_CHAINS_STEP)
    @Override
    public Step build() {
        return getStepBuilder().<String, List<EnrichmentChain>>chunk(5)
                .reader(enrichmentRequestReader)
                .processor(createChainsCompositeProcessor)
                .writer(enrichmentChainWriter)
                .build();
    }
}
