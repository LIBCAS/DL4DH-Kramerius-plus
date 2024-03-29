package cz.inqool.dl4dh.krameriusplus.core.request.enrichment.request;

import com.querydsl.jpa.impl.JPAQuery;
import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.store.DatedStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class EnrichmentRequestStore extends DatedStore<EnrichmentRequest, QEnrichmentRequest> {

    @Autowired
    public EnrichmentRequestStore(EntityManager entityManager) {
        super(EnrichmentRequest.class, QEnrichmentRequest.class, entityManager);
    }

    public Result<EnrichmentRequest> findByFilter(String publicationId, String name, String owner, int page, int pageSize) {
        JPAQuery<?> query = queryFactory.from(qObject);

        if (publicationId != null) {
            query.where(qObject.publicationIds.containsValue(publicationId));
        }

        if (name != null) {
            query = query.where(qObject.name.like(name));
        }

        if (owner != null) {
            query = query.where(qObject.owner.username.like(owner));
        }

        int count = query.select(qObject.count()).fetchOne().intValue();
        query.offset((long) page * pageSize);
        query.limit(pageSize);
        query.orderBy(qObject.created.desc());

        List<EnrichmentRequest> enrichmentRequests = query.select(qObject).fetch();

        return new Result<>(page, pageSize, count, enrichmentRequests);
    }
}
