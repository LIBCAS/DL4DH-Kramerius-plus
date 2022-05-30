package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.store.DomainStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class PageStore extends DomainStore<Page> {

    @Autowired
    public PageStore(MongoOperations mongoOperations) {
        super(Page.class, mongoOperations);
    }

    public QueryResults<Page> list(String publicationId, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "index"));

        Query query = Query.query(where("parentId").is(publicationId));
        query.fields().exclude("tokens");

        long total = mongoOperations.count(query, type);

        return new QueryResults<>(mongoOperations.find(query.with(pageRequest), type), (long) pageSize, (long) page * pageSize, total);
    }
}
