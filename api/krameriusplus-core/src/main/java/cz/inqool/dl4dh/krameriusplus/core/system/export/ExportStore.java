package cz.inqool.dl4dh.krameriusplus.core.system.export;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.store.DatedStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public class ExportStore extends DatedStore<Export, QExport> {

    @Autowired
    public ExportStore() {
        super(Export.class, QExport.class);
    }

    public List<Export> listDeletedOlderThan(Instant createdBefore) {
        return query()
                .select(qObject)
                .where(qObject.deleted.isNull())
                .where(qObject.created.before(createdBefore))
                .fetch();
    }

    public QueryResults<Export> list(String publicationId, int page, int pageSize) {
        JPAQuery<Export> exportQuery = query()
                .select(qObject)
                .where(qObject.deleted.isNull());

        if (publicationId != null) {
            exportQuery.where(qObject.publicationId.eq(publicationId));
        }

        return exportQuery
                .offset((long) page * pageSize)
                .limit(pageSize)
                .fetchResults();
    }
}
