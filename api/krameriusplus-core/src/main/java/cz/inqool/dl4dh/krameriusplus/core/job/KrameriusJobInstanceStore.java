package cz.inqool.dl4dh.krameriusplus.core.job;

import cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.store.DomainStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus.CANCELLED;

@Repository
public class KrameriusJobInstanceStore extends DomainStore<KrameriusJobInstance, QKrameriusJobInstance> {

    @Autowired
    public KrameriusJobInstanceStore(EntityManager em) {
        super(KrameriusJobInstance.class, QKrameriusJobInstance.class, em);
    }


    @Override
    public Optional<KrameriusJobInstance> findById(String id) {
        return Optional.of(query().setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .select(qObject)
                .where(qObject.id.eq(id))
                .fetchFirst());
    }

    @Transactional
    public List<KrameriusJobInstance> cancelStartableJobs(Set<String> ids) {
        long updatedCount = queryFactory
                .update(qObject)
                .where(qObject.id.in(ids).and(qObject.executionStatus.in(ExecutionStatus.Sets.startable)))
                .set(qObject.executionStatus, CANCELLED)
                .execute();

        List<KrameriusJobInstance> cancelled = query().select(qObject)
                .where(qObject.id.in(ids).and(qObject.executionStatus.eq(CANCELLED)))
                .fetch();

        if (cancelled.size() != updatedCount) {
            throw new IllegalStateException("Retrieved and updated object counts do not match.");
        }

        return cancelled;
    }
}
