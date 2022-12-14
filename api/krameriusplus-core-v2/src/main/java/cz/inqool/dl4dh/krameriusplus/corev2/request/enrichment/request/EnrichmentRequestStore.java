package cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.request;

import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.store.DatedStore;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class EnrichmentRequestStore extends DatedStore<EnrichmentRequest, QEnrichmentRequest> {

    public EnrichmentRequestStore(EntityManager entityManager) {
        super(EnrichmentRequest.class, QEnrichmentRequest.class, entityManager);
    }
}
