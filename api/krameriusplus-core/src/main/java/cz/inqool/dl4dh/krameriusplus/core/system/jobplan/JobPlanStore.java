package cz.inqool.dl4dh.krameriusplus.core.system.jobplan;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.store.DatedStore;
import org.springframework.stereotype.Repository;

@Repository
public class JobPlanStore extends DatedStore<JobPlan, QJobPlan> {

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
