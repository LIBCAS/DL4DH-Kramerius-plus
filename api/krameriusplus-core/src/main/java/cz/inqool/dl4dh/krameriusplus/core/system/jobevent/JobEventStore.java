package cz.inqool.dl4dh.krameriusplus.core.system.jobevent;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.store.DatedStore;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJob;
import org.springframework.batch.core.BatchStatus;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class JobEventStore extends DatedStore<JobEvent, QJobEvent> {

    public JobEventStore() {
        super(JobEvent.class, QJobEvent.class);
    }

    public QueryResults<JobEvent> listJobsByType(Set<KrameriusJob> jobTypes, String publicationId, int page, int pageSize) {
        JPAQuery<JobEvent> query = query()
                .select(qObject)
                .where(qObject.krameriusJob.in(jobTypes))
                .where(qObject.deleted.isNull())
                .orderBy(qObject.created.desc())
                .limit(pageSize)
                .offset((long) page * pageSize);

        if (publicationId != null) {
            query.where(qObject.publicationId.eq(publicationId));
        }

        QueryResults<JobEvent> result = query.fetchResults();

        detachAll();

        return result;
    }

    public void updateJobStatus(String jobEventId, BatchStatus status) {
        entityManager.createQuery("UPDATE JobEvent j SET j.lastExecutionStatus = :exec WHERE j.id=:id")
                .setParameter("exec", status)
                .setParameter("id", jobEventId)
                .executeUpdate();
    }
}
