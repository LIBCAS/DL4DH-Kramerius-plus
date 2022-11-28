package cz.inqool.dl4dh.krameriusplus.corev2.request.export.export;

import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.store.DatedStore;
import org.springframework.stereotype.Repository;

@Repository
public class ExportStore extends DatedStore<Export, QExport> {

    public ExportStore() {
        super(Export.class, QExport.class);
    }
}
