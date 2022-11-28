package cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page.store;

import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.document.DomainDocumentStore;
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

    public List<Page> listByPublication(String publicationId) {
        Query query = Query.query(where("parentId").is(publicationId));
        query.fields().exclude("tokens");
        query.fields().exclude("nameTagMetadata");
        query.with(Sort.by(Sort.Direction.ASC, "index"));

        return mongoOperations.find(query, type);
    }
}
