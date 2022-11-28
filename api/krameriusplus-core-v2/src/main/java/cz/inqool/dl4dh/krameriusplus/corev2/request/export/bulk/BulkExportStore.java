package cz.inqool.dl4dh.krameriusplus.corev2.request.export.bulk;

import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.store.DatedStore;
import org.springframework.stereotype.Repository;

@Repository
public class BulkExportStore extends DatedStore<BulkExport, QBulkExport> {

    public BulkExportStore() {
        super(BulkExport.class, QBulkExport.class);
    }
}
