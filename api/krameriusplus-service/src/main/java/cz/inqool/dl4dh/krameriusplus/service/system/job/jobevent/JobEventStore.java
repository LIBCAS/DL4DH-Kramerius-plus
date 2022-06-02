package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.store.DatedStore;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobconfig.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.JobPlan;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.QScheduledJobEvent;
import org.springframework.stereotype.Repository;

import java.util.Set;

import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

@Repository
public class JobEventStore extends DatedStore<JobEvent, QJobEvent> {

    public JobEventStore() {
        super(JobEvent.class, QJobEvent.class);
    }

    public QueryResults<JobEvent> listJobsByType(Set<KrameriusJob> jobTypes, String publicationId, int page, int pageSize) {
        JPAQuery<JobEvent> query = query()
                .select(qObject)
                .where(qObject.config.krameriusJob.in(jobTypes))
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

    public void updateJobStatus(String jobEventId, JobStatus status) {
        entityManager.createQuery("UPDATE JobEvent j SET j.lastExecutionStatus = :exec WHERE j.id=:id")
                .setParameter("exec", status)
                .setParameter("id", jobEventId)
                .executeUpdate();
    }

    public void updateJobRun(String jobEventId, Long instanceId, Long lastExecutionId) {
        entityManager.createQuery("UPDATE JobEvent j SET j.instanceId=:instanceId, j.lastExecutionId=:executionId WHERE j.id=:id")
                .setParameter("instanceId", instanceId)
                .setParameter("executionId", lastExecutionId)
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

    public JobPlan findExecutionPlanByJobEventId(String jobEventId) {
        QScheduledJobEvent qScheduledJobEvent = QScheduledJobEvent.scheduledJobEvent;

        return queryFactory.from(qScheduledJobEvent)
                .select(qScheduledJobEvent.jobPlan)
                .where(qScheduledJobEvent.jobEvent.id.eq(jobEventId))
                .fetchOne();
    }
}