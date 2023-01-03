package cz.inqool.dl4dh.krameriusplus.core.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.core.request.enrichment.item.EnrichmentRequestItem;
import lombok.NonNull;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@StepScope
@Component
public class EnrichmentItemCreatingProcessor implements ItemProcessor<String, EnrichmentRequestItem> {

    private final PublicationStore publicationStore;

    private Long orderCounter = 0L;

    @Autowired
    public EnrichmentItemCreatingProcessor(PublicationStore publicationStore) {
        this.publicationStore = publicationStore;
    }

    @Override
    public EnrichmentRequestItem process(@NonNull String item) throws Exception {
        Publication publication = publicationStore.findById(item).orElseThrow(
                () -> new IllegalStateException("Publication with id: " + item + " has not been initialized"));

        EnrichmentRequestItem enrichmentRequestItem = new EnrichmentRequestItem();
        enrichmentRequestItem.setPublicationId(publication.getId());
        enrichmentRequestItem.setPublicationTitle(publication.getTitle());
        enrichmentRequestItem.setModel(publication.getModel());
        enrichmentRequestItem.setOrder(orderCounter++);

        return enrichmentRequestItem;
    }
}
