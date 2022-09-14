package cz.inqool.dl4dh.krameriusplus.core.system.bulkexport;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.store.DatedStore;
import org.springframework.stereotype.Repository;

@Repository
public class BulkExportStore extends DatedStore<BulkExport, QBulkExport> {
    public BulkExportStore() {
        super(BulkExport.class, QBulkExport.class);
    }
}
