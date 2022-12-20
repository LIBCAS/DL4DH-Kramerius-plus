package cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.chain;

import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.store.DomainStore;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class EnrichmentChainStore extends DomainStore<EnrichmentChain, QEnrichmentChain> {

    public EnrichmentChainStore(EntityManager em) {
        super(EnrichmentChain.class, QEnrichmentChain.class, em);
    }

    public EnrichmentChain findByKrameriusJobInstance(KrameriusJobInstance jobInstance) {
        return query()
                .select(qObject)
                .where(qObject.jobs.containsValue(jobInstance))
                .fetchOne();
    }
}
