package cz.inqool.dl4dh.krameriusplus.core.system.export;


import com.querydsl.jpa.impl.JPAQuery;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.store.DatedStore;
import org.springframework.stereotype.Repository;

@Repository
public class MergedExportStore extends DatedStore<MergedExport, QMergedExport> {
    public MergedExportStore() {
        super(MergedExport.class, QMergedExport.class);
    }

    public MergedExport findByJobEventId(String jobEventId) {
        JPAQuery<MergedExport> query = query()
                .select(qObject)
                .where(qObject.jobEvent.id.eq(jobEventId));

        return query.fetchFirst();
    }
}
