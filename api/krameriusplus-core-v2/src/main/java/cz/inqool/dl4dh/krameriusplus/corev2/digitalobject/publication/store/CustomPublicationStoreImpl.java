package cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.store;

import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.document.DomainDocument;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.document.DomainDocumentStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class CustomPublicationStoreImpl extends DomainDocumentStore<Publication> implements CustomPublicationStore {

    @Autowired
    public CustomPublicationStoreImpl(MongoOperations mongoOperations) {
        super(mongoOperations, Publication.class);
    }

    @Override
    public List<Publication> findAllPublishedModified(Instant publishedModifiedAfter) {
        Query query = query(where("publishInfo.publishedLastModified").gte(publishedModifiedAfter));

        return mongoOperations.find(query, type);
    }

    @Override
    public List<Publication> findAllChildren(String parentId) {
        Query query = query(where("parentId").is(parentId));
        query.with(Sort.by(Sort.Direction.ASC, "index"));

        return mongoOperations.find(query, type);
    }

    @Override
    public List<String> findAllChildrenIds(String parentId) {
        Query query = query(where("parentId").is(parentId));
        query.fields().include("_id", "_class");
        query.with(Sort.by(Sort.Direction.ASC, "index"));

        return mongoOperations.find(query, type).stream().map(DomainDocument::getId).collect(Collectors.toList());
    }

    /**
     * Fetches publication tree for given root. The tree includes all the child publications, which in turn
     * include all their children. Do not include pages.
     *
     * @param publicationId root publication ID
     * @return Publication object with children tree
     */
    @Override
    public Publication findPublicationTree(String publicationId) {
        Query query = query(where("_id").is(publicationId));

        Publication root = mongoOperations.find(query, Publication.class).get(0);

        getChildrenRec(root);

        return root;
    }

    private void getChildrenRec(Publication parent) {
        if (parent == null) {
            return;
        }

        List<Publication> children = mongoOperations.find(createChildQuery(parent.getId()), Publication.class);

        parent.setChildren(children);

        children.forEach(this::getChildrenRec);
    }


    private Query createChildQuery(String parentId) {
        Query childQuery = query(where("parentId").is(parentId));
        childQuery.with(Sort.by(Sort.Direction.ASC, "index"));

        return childQuery;
    }
}


