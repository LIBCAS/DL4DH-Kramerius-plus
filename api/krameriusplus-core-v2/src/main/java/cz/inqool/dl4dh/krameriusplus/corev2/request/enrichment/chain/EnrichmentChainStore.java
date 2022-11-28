package cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.chain;

import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.store.DomainStore;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import org.springframework.stereotype.Repository;

@Repository
public class EnrichmentChainStore extends DomainStore<EnrichmentChain, QEnrichmentChain> {

    public EnrichmentChainStore() {
        super(EnrichmentChain.class, QEnrichmentChain.class);
    }

    public EnrichmentChain findByKrameriusJobInstance(KrameriusJobInstance jobInstance) {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }
}
