package cz.inqool.dl4dh.krameriusplus.corev2.job.config.enrichment;

import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.store.DomainStore;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class EnrichmentJobConfigStore extends DomainStore<EnrichmentJobConfig, QEnrichmentJobConfig> {

    public EnrichmentJobConfigStore(EntityManager em) {
        super(EnrichmentJobConfig.class, QEnrichmentJobConfig.class, em);
    }
}
