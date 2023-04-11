package cz.inqool.dl4dh.krameriusplus.core.request.export.request;

import com.querydsl.jpa.impl.JPAQuery;
import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.store.DatedStore;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.core.request.export.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.request.export.item.QExportRequestItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class ExportRequestStore extends DatedStore<ExportRequest, QExportRequest> {

    @Autowired
    public ExportRequestStore(EntityManager em) {
        super(ExportRequest.class, QExportRequest.class, em);
    }

    /**
     * Finds mergeJob associated with ExportRequest. The exportRequest is found
     * by exportItem, which has the given export set as the rootExport
     * @param export
     * @return
     */
    public KrameriusJobInstance findMergeJob(Export export) {
        QExportRequestItem qExportRequestItem = QExportRequestItem.exportRequestItem;

        // TODO: rewrite with a join query
        return queryFactory.from(qExportRequestItem)
                .select(qExportRequestItem)
                .where(qExportRequestItem.rootExport.eq(export))
                .fetchOne()
                .getExportRequest()
                .getBulkExport()
                .getMergeJob();
    }

    public Result<ExportRequestView> findByNameOwnerAndStatus(String name, String owner, Boolean isFinished, int page, int pageSize) {
        QExportRequestView exportRequestView = QExportRequestView.exportRequestView;
        JPAQuery<?> query = queryFactory.from(exportRequestView);

        if (name != null) {
            query = query.where(exportRequestView.name.eq(name));
        }
        if (owner != null) {
            query = query.where(exportRequestView.owner.username.eq(owner));

        }
        if (isFinished != null) {
            query = query.where(isFinished ? exportRequestView.bulkExport.isNotNull() : exportRequestView.bulkExport.isNull());

        }
        int count = query.select(exportRequestView.count()).fetchOne().intValue();
        query.offset((long) page * pageSize);
        query.limit(pageSize);
        query.orderBy(exportRequestView.created.desc());

        List<ExportRequestView> results = query.select(QExportRequestView.exportRequestView).fetch();

        return new Result<>(page, pageSize, count, results);
    }

    public ExportRequest findByRootExport(Export root) {
        ExportRequest exportRequest = query()
                .from(qObject)
                .select(qObject)
                .where(qObject.items.any().rootExport.eq(root))
                .fetchOne();

        return exportRequest;
    }
}
