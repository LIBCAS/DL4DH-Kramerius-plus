package cz.inqool.dl4dh.krameriusplus.corev2.request.export.request;

import com.querydsl.jpa.impl.JPAQuery;
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

        return queryFactory.from(qExportRequestItem)
                .select(qExportRequestItem.exportRequest.bulkExport.mergeJob)
                .where(qExportRequestItem.rootExport.eq(export))
                .fetchOne();
    }

    public List<ExportRequest> findByNameOwnerAndStatus(String name, String owner, Boolean isFinished, int page, int pageSize) {
        JPAQuery<ExportRequest> query = queryFactory.from(qObject)
                .select(qObject);

        if (name != null) {
            query = query.where(qObject.name.eq(name));
        }
        if (owner != null) {
            query = query.where(qObject.owner.username.eq(owner));

        }
        if (isFinished != null) {
            query = query.where(isFinished ? qObject.bulkExport.isNotNull() : qObject.bulkExport.isNull());

        }

        query.offset((long) page * pageSize);
        query.limit(pageSize);

        return query.fetch();
    }
}
