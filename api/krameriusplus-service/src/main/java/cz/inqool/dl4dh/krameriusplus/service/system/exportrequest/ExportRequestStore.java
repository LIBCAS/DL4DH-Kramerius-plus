package cz.inqool.dl4dh.krameriusplus.service.system.exportrequest;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.store.OwnedObjectStore;
import cz.inqool.dl4dh.krameriusplus.core.system.jobplan.QJobPlan;
import cz.inqool.dl4dh.krameriusplus.core.system.jobplan.QScheduledJobEvent;
import org.springframework.stereotype.Repository;

@Repository
public class ExportRequestStore extends OwnedObjectStore<ExportRequest, QExportRequest> {

    public ExportRequestStore() {
        super(ExportRequest.class, QExportRequest.class);
    }

    public ExportRequest findByJobEventId(String jobEventId) {
        QScheduledJobEvent qScheduledJobEvent = QScheduledJobEvent.scheduledJobEvent;
        String scheduledJobEventId = queryFactory.from(qScheduledJobEvent)
                .select(qScheduledJobEvent.id)
                .where(qScheduledJobEvent.jobEvent.id.eq(jobEventId))
                .fetchOne();

        QJobPlan qJobPlan = QJobPlan.jobPlan;

        String jobPlanId = queryFactory.from(qJobPlan)
                .select(qJobPlan.id)
                .where(qJobPlan.scheduledJobEvents.any().id.eq(scheduledJobEventId))
                .fetchOne();

        ExportRequest exportRequest = query()
                .select(qObject)
                .where(qObject.jobPlan.id.eq(jobPlanId))
                .fetchFirst();

        detachAll();

        return exportRequest;
    }
}
