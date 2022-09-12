package cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest;


import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.store.OwnedObjectStore;
import org.springframework.stereotype.Repository;

@Repository
public class ExportRequestStore extends OwnedObjectStore<ExportRequest, QExportRequest> {

    public ExportRequestStore() {
        super(ExportRequest.class, QExportRequest.class);
    }
}
