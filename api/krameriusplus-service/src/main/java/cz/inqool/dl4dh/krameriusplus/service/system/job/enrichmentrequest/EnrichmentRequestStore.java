package cz.inqool.dl4dh.krameriusplus.service.system.job.enrichmentrequest;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.store.OwnedObjectStore;
import org.springframework.stereotype.Repository;

@Repository
public class EnrichmentRequestStore extends OwnedObjectStore<EnrichmentRequest, QEnrichmentRequest> {

    public EnrichmentRequestStore() {
        super(EnrichmentRequest.class, QEnrichmentRequest.class);
    }
}
