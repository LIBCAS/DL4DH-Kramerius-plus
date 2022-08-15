package cz.inqool.dl4dh.krameriusplus.core.system.jobevent;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.store.DatedStore;
import org.springframework.stereotype.Repository;

import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

@Repository
public class JobEventStore extends DatedStore<JobEvent, QJobEvent> {

    public JobEventStore() {
        super(JobEvent.class, QJobEvent.class);
    }

    public QueryResults<JobEvent> listJobs(JobEventFilter filter, int page, int pageSize) {
        JPAQuery<JobEvent> query = query()
                .select(qObject);

        if (filter.getLastExecutionStatus() != null) {
            query.where(qObject.details.lastExecutionStatus.eq(filter.getLastExecutionStatus()));
        }

        if (filter.getKrameriusJobs() != null && !filter.getKrameriusJobs().isEmpty()) {
            query.where(qObject.config.krameriusJob.in(filter.getKrameriusJobs()));
        }

        if (filter.getPublicationId() != null) {
            query.where(qObject.publicationId.eq(filter.getPublicationId()));
        }

        if (!filter.isIncludeDeleted()) {
            query.where(qObject.deleted.isNull());
        }

        query.orderBy(qObject.created.desc())
                .limit(pageSize)
                .offset((long) page * pageSize);

        QueryResults<JobEvent> result = query.fetchResults();

        detachAll();

        return result;
    }

    public void updateJobStatus(String jobEventId, JobStatus status) {
        entityManager.createQuery("UPDATE JobEvent j SET j.details.lastExecutionStatus = :exec WHERE j.id=:id")
                .setParameter("exec", status)
                .setParameter("id", jobEventId)
                .executeUpdate();
    }

    public boolean existsOtherJobs(String publicationId, String excludeJobEventId, KrameriusJob jobType) {
        Long count = query().select(qObject.count())
                .where(qObject.deleted.isNull())
                .where(qObject.config.krameriusJob.eq(jobType))
                .where(qObject.publicationId.eq(publicationId))
                .where(qObject.id.ne(excludeJobEventId))
                .fetchOne();

        notNull(count, () -> new IllegalStateException("Count query should never return null"));

        return count > 0;
    }

    public Long getDependency(String publicationId, KrameriusJob prerequisite) {
        JPAQuery<Long> query = query()
                .select(qObject.count())
                .where(qObject.publicationId.eq(publicationId))
                .where(qObject.config.krameriusJob.eq(prerequisite))
                .where(qObject.deleted.isNull())
                .where(qObject.details.lastExecutionStatus.eq(JobStatus.COMPLETED));

        return query.fetchFirst();
    }
}
