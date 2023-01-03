package cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.request;

import com.querydsl.jpa.impl.JPAQuery;
import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.store.DatedStore;
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

    public Result<EnrichmentRequest> findByNameAndOwner(String name, String owner, int page, int pageSize) {
        JPAQuery<?> query = queryFactory.from(qObject);
        if (name != null) {
            query = query.where(qObject.name.eq(name));
        }

        if (owner != null) {
            query = query.where(qObject.owner.username.eq(owner));
        }

        int count = query.select(qObject.count()).fetchOne().intValue();
        query.offset((long) page * pageSize);
        query.limit(pageSize);
        List<EnrichmentRequest> enrichmentRequests = query.select(qObject).fetch();

        return new Result<>(pageSize, page, count, enrichmentRequests);
    }
}
