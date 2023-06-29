package cz.inqool.dl4dh.krameriusplus.core.request.export.export;

import cz.inqool.dl4dh.krameriusplus.api.export.ExportState;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.store.DatedStore;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstance;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;

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

    @Transactional
    public void cancelExports(Set<String> exportIds) {
        long updatedCount  = queryFactory
                .update(qObject)
                .where(qObject.id.in(exportIds).and(qObject.state.eq(ExportState.CREATED)))
                .set(qObject.state, ExportState.CANCELLED)
                .execute();

        List<Export> cancelled = query().select(qObject)
                .where(qObject.id.in(exportIds).and(qObject.state.eq(ExportState.CANCELLED)))
                .fetch();

        if (cancelled.size() != updatedCount) {
            throw new IllegalStateException("Retrieved and updated object counts do not match.");
        }
    }
}
