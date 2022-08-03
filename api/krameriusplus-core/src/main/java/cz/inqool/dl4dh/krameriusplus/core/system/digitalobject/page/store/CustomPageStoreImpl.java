package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.store;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.store.AbstractMongoStore;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.store.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public QueryResults<Page> findAllByPublication(String publicationId, Pageable pageRequest) {
        if (pageRequest.getSort().equals(Sort.unsorted())) {
            pageRequest = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(),
                    Sort.by(Sort.Direction.ASC, "index"));
        }

        Query query = Query.query(where("parentId").is(publicationId));
        query.fields().exclude("tokens");
        query.fields().exclude("nameTagMetadata");

        long count = mongoOperations.count(query, type);

        List<Page> result = mongoOperations.find(query.with(pageRequest), type);

        return constructQueryResults(result, pageRequest, count);
    }
}
