package cz.inqool.dl4dh.krameriusplus.core.request.enrichment.item;

import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.store.DomainStore;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class EnrichmentRequestItemStore extends DomainStore<EnrichmentRequestItem, QEnrichmentRequestItem> {

    public EnrichmentRequestItemStore(EntityManager em) {
        super(EnrichmentRequestItem.class, QEnrichmentRequestItem.class, em);
    }

    public EnrichmentRequestItem find(String enrichmentRequestId, String publicationId) {
        return query().select(qObject)
                .where(qObject.enrichmentRequest.id.eq(enrichmentRequestId))
                .where(qObject.publicationId.eq(publicationId))
                .fetchOne();
    }
}
