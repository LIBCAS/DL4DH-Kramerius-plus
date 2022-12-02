package cz.inqool.dl4dh.krameriusplus.corev2.batch.components;

import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.item.EnrichmentRequestItem;
import lombok.NonNull;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@StepScope
@Component
public class EnrichmentRequestItemCreatingProcessor implements ItemProcessor<String, EnrichmentRequestItem> {

    private final PublicationStore publicationStore;

    private Long orderCounter = 0L;

    @Autowired
    public EnrichmentRequestItemCreatingProcessor(PublicationStore publicationStore) {
        this.publicationStore = publicationStore;
    }

    @Override
    public EnrichmentRequestItem process(@NonNull String item) throws Exception {
        Publication publication = publicationStore.findById(item)
                .orElseThrow(() -> new IllegalStateException(String.format("Publication with id %s not initialized", item)));

        EnrichmentRequestItem enrichmentRequestItem = new EnrichmentRequestItem();
        enrichmentRequestItem.setPublicationId(item);
        enrichmentRequestItem.setOrder(orderCounter++);

        return enrichmentRequestItem;
    }
}
