package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.store;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.store.AbstractMongoStore;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class CustomPageStoreImpl extends AbstractMongoStore<Page> implements CustomPageStore {
    public CustomPageStoreImpl(MongoOperations mongoOperations) {
        super(mongoOperations, Page.class);
    }

    public QueryResults<Page> list(String publicationId, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "index"));

        Query query = Query.query(where("parentId").is(publicationId));
        query.fields().exclude("tokens");
        query.fields().exclude("nameTagMetadata");

        long total = mongoOperations.count(query, type);

        return new QueryResults<>(mongoOperations.find(query.with(pageRequest), type), (long) pageSize, (long) page * pageSize, total);
    }

    public List<Page> listWithTei(String publicationId) {
        Query query = Query.query(where("parentId").is(publicationId));

        query.fields().include("_class", "_id", "teiBodyFileId");

        return mongoOperations.find(query, type);
    }
}
