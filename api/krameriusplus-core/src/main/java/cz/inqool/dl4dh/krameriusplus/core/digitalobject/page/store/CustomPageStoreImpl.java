package cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.store;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.domain.document.DomainDocumentStore;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class CustomPageStoreImpl extends DomainDocumentStore<Page> implements CustomPageStore {

    public CustomPageStoreImpl(MongoOperations mongoOperations) {
        super(mongoOperations, Page.class);
    }

    @Override
    public Result<Page> list(String publicationId, int page, int pageSize) {
        Query query = Query.query(where("parentId").is(publicationId));
        query.fields().exclude("tokens");
        query.fields().exclude("nameTagMetadata");
        query.with(Sort.by(Sort.Direction.ASC, "index"));

        long total = mongoOperations.count(query, type);

        query.with(PageRequest.of(page, pageSize));

        List<Page> pages = mongoOperations.find(query, type);

        return new Result<>(page, pageSize, total, pages);
    }
}
