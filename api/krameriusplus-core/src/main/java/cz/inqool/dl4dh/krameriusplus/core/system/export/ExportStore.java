package cz.inqool.dl4dh.krameriusplus.core.system.export;

import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.store.DatedStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ExportStore extends DatedStore<Export, QExport> {

    @Autowired
    public ExportStore() {
        super(Export.class, QExport.class);
    }
}
