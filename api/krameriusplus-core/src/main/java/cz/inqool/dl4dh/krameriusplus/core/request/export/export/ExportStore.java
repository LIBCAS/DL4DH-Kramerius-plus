package cz.inqool.dl4dh.krameriusplus.core.request.export.export;

import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.store.DatedStore;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstance;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class ExportStore extends DatedStore<Export, QExport> {

    public ExportStore(EntityManager em) {
        super(Export.class, QExport.class, em);
    }

    public Export findByExportJob(KrameriusJobInstance jobInstance) {
        return query().select(qObject)
                .where(qObject.deleted.isNull())
                .where(qObject.exportJob.eq(jobInstance))
                .fetchOne();
    }
}
