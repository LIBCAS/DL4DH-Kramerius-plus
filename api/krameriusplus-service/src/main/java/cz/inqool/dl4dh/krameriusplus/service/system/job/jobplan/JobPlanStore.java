package cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.store.OwnedObjectStore;
import org.springframework.stereotype.Repository;

@Repository
public class JobPlanStore extends OwnedObjectStore<JobPlan, QJobPlan> {

    public JobPlanStore() {
        super(JobPlan.class, QJobPlan.class);
    }

    public JobPlan findByJobEvent(String jobEventId) {
        QScheduledJobEvent qScheduledJobEvent = QScheduledJobEvent.scheduledJobEvent;

        JobPlan jobPlan = queryFactory.from(qScheduledJobEvent)
                .select(qScheduledJobEvent.jobPlan)
                .where(qScheduledJobEvent.jobEvent.id.eq(jobEventId))
                .fetchOne();

        detachAll();

        return jobPlan;
    }
}
