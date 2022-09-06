package cz.inqool.dl4dh.krameriusplus.core.system.export;


import com.querydsl.jpa.impl.JPAQuery;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.store.DatedStore;
import org.springframework.stereotype.Repository;

@Repository
public class BulkExportStore extends DatedStore<BulkExport, QBulkExport> {
    public BulkExportStore() {
        super(BulkExport.class, QBulkExport.class);
    }

    public BulkExport findByJobEventId(String jobEventId) {
        JPAQuery<BulkExport> query = query()
                .select(qObject)
                .where(qObject.jobEvent.id.eq(jobEventId));

        BulkExport result = query.fetchFirst();

        detachAll();

        return result;
    }
}
