package cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.request;

import com.querydsl.jpa.impl.JPAQuery;
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

    public List<EnrichmentRequest> findByNameAndOwner(String name, String owner, int page, int pageSize) {
        JPAQuery<EnrichmentRequest> query = queryFactory.from(qObject)
                .select(qObject)
                .where(qObject.name.eq(name))
                .where(qObject.owner.username.eq(owner));

        query.offset(page);
        query.limit(pageSize);

        return query.fetch();
    }
}
