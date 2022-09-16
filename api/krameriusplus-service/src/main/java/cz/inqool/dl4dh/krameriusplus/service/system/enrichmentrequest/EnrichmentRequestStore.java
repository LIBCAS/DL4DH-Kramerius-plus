package cz.inqool.dl4dh.krameriusplus.service.system.enrichmentrequest;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.store.OwnedObjectStore;
import org.springframework.stereotype.Repository;

@Repository
public class EnrichmentRequestStore extends OwnedObjectStore<EnrichmentRequest, QEnrichmentRequest> {

    public EnrichmentRequestStore() {
        super(EnrichmentRequest.class, QEnrichmentRequest.class);
    }

    public QueryResults<EnrichmentRequest> list(String name, String owner, int page, int pageSize) {
        JPAQuery<EnrichmentRequest> query = query()
                .select(qObject);

        if (name != null) {
            query.where(qObject.name.like('%' + name + '%'));
        }

        if (owner != null) {
            query.where(qObject.owner.username.like('%' + owner + '%'));
        }

        query.orderBy(qObject.created.desc())
                .limit(pageSize)
                .offset((long) page * pageSize);

        QueryResults<EnrichmentRequest> result = query.fetchResults();

        detachAll();

        return result;
    }
}
