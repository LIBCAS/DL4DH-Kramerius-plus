package cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.item;

import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.store.DomainStore;
import org.springframework.stereotype.Repository;

@Repository
public class EnrichmentRequestItemStore extends DomainStore<EnrichmentRequestItem, QEnrichmentRequestItem> {

    public EnrichmentRequestItemStore() {
        super(EnrichmentRequestItem.class, QEnrichmentRequestItem.class);
    }
}
