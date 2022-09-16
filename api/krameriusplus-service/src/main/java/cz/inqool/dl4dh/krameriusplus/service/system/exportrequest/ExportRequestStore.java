package cz.inqool.dl4dh.krameriusplus.service.system.exportrequest;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
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

    public QueryResults<ExportRequest> list(String name, String owner, Boolean isFinished, int page, int pageSize) {
        JPAQuery<ExportRequest> query = query()
                .select(qObject);

        if (name != null) {
            query.where(qObject.name.like('%' + name + '%'));
        }

        if (owner != null) {
            query.where(qObject.owner.username.like('%' + owner + '%'));
        }

        if (isFinished != null) {
            query.where(qObject.bulkExport.fileRef.isNotNull());
        }

        query.orderBy(qObject.created.desc())
                .limit(pageSize)
                .offset((long) page * pageSize);

        QueryResults<ExportRequest> result = query.fetchResults();

        detachAll();

        return result;
    }
}
