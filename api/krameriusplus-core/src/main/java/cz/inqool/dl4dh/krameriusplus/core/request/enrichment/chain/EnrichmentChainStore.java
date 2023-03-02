package cz.inqool.dl4dh.krameriusplus.core.request.enrichment.chain;

import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.store.DomainStore;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.core.job.QKrameriusJobInstance;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

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

    /**
     * Find all jobs of same type on specific publication id
     *
     * @param publicationId of enrichment chain
     * @param krameriusJobType type of job
     * @return list of jobs on same publication id and same type
     */
    public List<KrameriusJobInstance> findMatchingJobs(String publicationId, KrameriusJobType krameriusJobType) {
        QKrameriusJobInstance qKrameriusJobInstance = QKrameriusJobInstance.krameriusJobInstance;
        return query()
                .select(qKrameriusJobInstance)
                .from(qObject)
                .innerJoin(qObject.jobs, qKrameriusJobInstance)
                .where(qObject.publicationId.eq(publicationId))
                .where(qKrameriusJobInstance.jobType.eq(krameriusJobType))
                .fetch();
    }
}
