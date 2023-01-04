package cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.store;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.publication.PublicationFilter;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.domain.document.DomainDocumentStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class CustomPublicationStoreImpl extends DomainDocumentStore<Publication> implements CustomPublicationStore {

    @Autowired
    public CustomPublicationStoreImpl(MongoOperations mongoOperations) {
        super(mongoOperations, Publication.class);
    }

    @Override
    public List<Publication> findAllChildren(String parentId) {
        Query query = query(where("parentId").is(parentId));
        query.with(Sort.by(Sort.Direction.ASC, "index"));

        return mongoOperations.find(query, type);
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

    @Override
    public Result<Publication> list(PublicationFilter filter, int page, int pageSize) {
        Query query = new Query();

        if (filter.getUuid() != null) {
          query.addCriteria(where("_id").regex(filter.getUuid()));
        } if (filter.getTitle() != null) {
            query.addCriteria(where("title").regex(filter.getTitle()));
        } if (filter.getIsRootEnrichment() != null) {
            query.addCriteria(where("isRootEnrichment").is(filter.getIsRootEnrichment()));
        } if (filter.getParentId() != null) {
            query.addCriteria(where("parentId").is(filter.getParentId()));
        } if (filter.getModel() != null) {
            query.addCriteria(where("_class").is(filter.getModel().getName()));
        } if (filter.getCreatedBefore() != null) {
            query.addCriteria(where("created").lte(filter.getCreatedBefore()));
        } if (filter.getCreatedAfter() != null) {
            query.addCriteria(where("created").gte(filter.getCreatedAfter()));
        } if (filter.getIsPublished() != null) {
            query.addCriteria(where("publishInfo.isPublished").is(filter.getIsPublished()));
        } if (filter.getPublishedBefore() != null) {
            query.addCriteria(where("publishInfo.publishedLastModified").lte(filter.getPublishedBefore()));
        } if (filter.getPublishedAfter() != null) {
            query.addCriteria(where("publishInfo.publishedLastModified").gte(filter.getPublishedAfter()));
        }

        long total = mongoOperations.count(query, Publication.class);

        query.with(Sort.by(Sort.Direction.DESC, "created"));
        query.with(PageRequest.of(page, pageSize));

        List<Publication> publications = mongoOperations.find(query, Publication.class);

        return new Result<>(pageSize, page, total, publications);
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


