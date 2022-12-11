package cz.inqool.dl4dh.krameriusplus.corev2.request.export.export;

import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.store.DatedStore;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import org.springframework.stereotype.Repository;

@Repository
public class ExportStore extends DatedStore<Export, QExport> {

    public ExportStore() {
        super(Export.class, QExport.class);
    }

    public Export findByKrameriusJob(KrameriusJobInstance jobInstance) {
        Export export = query().select(qObject)
                .where(qObject.deleted.isNull())
                .where(qObject.exportJob.eq(jobInstance))
                .fetchOne();

        detach(export);

        return export;
    }
}
