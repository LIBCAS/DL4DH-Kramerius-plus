package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.item.EnrichmentRequestItem;
import lombok.NonNull;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@StepScope
@Component
public class EnrichmentItemCreatingProcessor implements ItemProcessor<String, EnrichmentRequestItem> {

    private Long orderCounter = 0L;

    @Override
    public EnrichmentRequestItem process(@NonNull String item) throws Exception {
        EnrichmentRequestItem enrichmentRequestItem = new EnrichmentRequestItem();
        enrichmentRequestItem.setPublicationId(item);
        enrichmentRequestItem.setOrder(orderCounter++);

        return enrichmentRequestItem;
    }
}
