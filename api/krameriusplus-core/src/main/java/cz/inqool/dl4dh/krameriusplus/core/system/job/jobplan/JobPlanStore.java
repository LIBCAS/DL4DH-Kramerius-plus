package cz.inqool.dl4dh.krameriusplus.core.system.job.jobplan;

import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.store.DatedStore;
import org.springframework.stereotype.Repository;

@Repository
public class JobPlanStore extends DatedStore<JobPlan, QJobPlan> {

    public JobPlanStore() {
        super(JobPlan.class, QJobPlan.class);
    }
}
