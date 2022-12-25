package cz.inqool.dl4dh.krameriusplus.corev2.request.export.request;

import com.querydsl.jpa.impl.JPAQuery;
import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.store.DatedStore;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.export.Export;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.item.QExportRequestItem;
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

    public Result<ExportRequest> findByNameOwnerAndStatus(String name, String owner, Boolean isFinished, int page, int pageSize) {
        JPAQuery<?> query = queryFactory.from(qObject);

        if (name != null) {
            query = query.where(qObject.name.eq(name));
        }
        if (owner != null) {
            query = query.where(qObject.owner.username.eq(owner));

        }
        if (isFinished != null) {
            query = query.where(isFinished ? qObject.bulkExport.isNotNull() : qObject.bulkExport.isNull());

        }
        int count = query.select(qObject.count()).fetchOne().intValue();
        query.offset((long) page * pageSize);
        query.limit(pageSize);

        List<ExportRequest> results = query.select(qObject).fetch();

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
