package cz.inqool.dl4dh.krameriusplus.corev2.request.export.request;

import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.store.DatedStore;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.export.Export;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.item.QExportRequestItem;
import org.springframework.stereotype.Repository;

@Repository
public class ExportRequestStore extends DatedStore<ExportRequest, QExportRequest> {

    public ExportRequestStore() {
        super(ExportRequest.class, QExportRequest.class);
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
}
