package cz.inqool.dl4dh.krameriusplus.core.system.jobevent;

import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.store.DatedStore;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJob;
import org.springframework.batch.core.BatchStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class JobEventStore extends DatedStore<JobEvent, QJobEvent> {

    public JobEventStore() {
        super(JobEvent.class, QJobEvent.class);
    }

    public List<JobEvent> listJobsByType(Set<KrameriusJob> jobTypes) {
        List<JobEvent> result = query()
                .select(qObject)
                .where(qObject.krameriusJob.in(jobTypes))
                .where(qObject.deleted.isNull())
                .orderBy(qObject.created.desc())
                .fetch();

        detachAll();

        return result;
    }

    public List<JobEvent> listJobsByTypeAndPublicationId(Set<KrameriusJob> jobTypes, String publicationId) {
        List<JobEvent> result = query()
                .select(qObject)
                .where(qObject.krameriusJob.in(jobTypes))
                .where(qObject.publicationId.eq(publicationId))
                .where(qObject.deleted.isNull())
                .orderBy(qObject.created.desc())
                .fetch();

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
