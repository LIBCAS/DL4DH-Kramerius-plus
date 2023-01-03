package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.processor;

import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.corev2.request.PublicationModel;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.item.EnrichmentRequestItem;
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
        enrichmentRequestItem.setModel(PublicationModel.fromString(publication.getModel()));
        enrichmentRequestItem.setOrder(orderCounter++);

        return enrichmentRequestItem;
    }
}
