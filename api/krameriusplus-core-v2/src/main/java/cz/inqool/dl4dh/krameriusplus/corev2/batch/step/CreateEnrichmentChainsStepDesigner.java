package cz.inqool.dl4dh.krameriusplus.corev2.batch.step;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor.EditionFindingProcessor;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor.EnrichmentChainCreatingProcessor;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.reader.EnrichmentRequestReader;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.writer.EnrichmentChainWriter;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.chain.EnrichmentChain;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.CREATE_ENRICHMENT_CHAINS_STEP;

@Component
public class CreateEnrichmentChainsStepDesigner extends AbstractStepDesigner {

    private final EnrichmentRequestReader enrichmentRequestReader;

    private final EditionFindingProcessor editionFindingProcessor;

    private final EnrichmentChainCreatingProcessor enrichmentChainCreatingProcessor;

    private final EnrichmentChainWriter enrichmentChainWriter;

    @Autowired
    public CreateEnrichmentChainsStepDesigner(EnrichmentRequestReader enrichmentRequestReader,
                                              EditionFindingProcessor editionFindingProcessor,
                                              EnrichmentChainCreatingProcessor enrichmentChainCreatingProcessor,
                                              EnrichmentChainWriter enrichmentChainWriter) {
        this.enrichmentRequestReader = enrichmentRequestReader;
        this.editionFindingProcessor = editionFindingProcessor;
        this.enrichmentChainCreatingProcessor = enrichmentChainCreatingProcessor;
        this.enrichmentChainWriter = enrichmentChainWriter;
    }

    @Override
    protected String getStepName() {
        return CREATE_ENRICHMENT_CHAINS_STEP;
    }

    @Override
    public Step build() {
        return getStepBuilder().<String, List<EnrichmentChain>>chunk(5)
                .reader(enrichmentRequestReader)
                .processor(new CompositeItemProcessorBuilder<String, List<EnrichmentChain>>()
                        .delegates(List.of(editionFindingProcessor, enrichmentChainCreatingProcessor))
                        .build())
                .writer(enrichmentChainWriter)
                .build();
    }
}
