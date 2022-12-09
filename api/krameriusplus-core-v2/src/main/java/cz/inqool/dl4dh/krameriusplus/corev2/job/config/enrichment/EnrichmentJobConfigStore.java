package cz.inqool.dl4dh.krameriusplus.corev2.job.config.enrichment;

import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.store.DomainStore;
import cz.inqool.dl4dh.krameriusplus.corev2.job.config.QEnrichmentJobConfig;
import org.springframework.stereotype.Repository;

@Repository
public class EnrichmentJobConfigStore extends DomainStore<EnrichmentJobConfig, QEnrichmentJobConfig> {

    public EnrichmentJobConfigStore() {
        super(EnrichmentJobConfig.class, QEnrichmentJobConfig.class);
    }
}
