package cz.inqool.dl4dh.krameriusplus.corev2.batch.step;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor.EnrichmentItemCreatingProcessor;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.reader.EnrichmentRequestReader;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.writer.EnrichmentItemWriter;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.item.EnrichmentRequestItem;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.CREATE_ENRICHMENT_ITEMS_STEP;

@Component
public class CreateEnrichmentItemsStepDesigner extends AbstractStepDesigner {

    private final EnrichmentRequestReader enrichmentRequestReader;

    private final EnrichmentItemCreatingProcessor enrichmentItemCreatingProcessor;

    private final EnrichmentItemWriter enrichmentItemWriter;

    @Autowired
    public CreateEnrichmentItemsStepDesigner(EnrichmentRequestReader enrichmentRequestReader,
                                             EnrichmentItemCreatingProcessor enrichmentItemCreatingProcessor,
                                             EnrichmentItemWriter enrichmentItemWriter) {
        this.enrichmentRequestReader = enrichmentRequestReader;
        this.enrichmentItemCreatingProcessor = enrichmentItemCreatingProcessor;
        this.enrichmentItemWriter = enrichmentItemWriter;
    }

    @Override
    protected String getStepName() {
        return CREATE_ENRICHMENT_ITEMS_STEP;
    }

    @Override
    public Step build() {
        return getStepBuilder().<String, EnrichmentRequestItem>chunk(5)
                .reader(enrichmentRequestReader)
                .processor(enrichmentItemCreatingProcessor)
                .writer(enrichmentItemWriter)
                .build();
    }
}
