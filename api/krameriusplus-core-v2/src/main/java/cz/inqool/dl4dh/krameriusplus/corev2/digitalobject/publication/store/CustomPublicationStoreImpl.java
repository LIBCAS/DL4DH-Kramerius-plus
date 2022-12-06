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

    @Override
    public List<String> findPublicationTree(String publicationId) {
//        List<String> result = mongoOperations.find(new Params() // get publication with id = publicationId if it has pages
//                .addFilters(new EqFilter("_id", publicationId))
//                .includeFields("_id", "_class")
//                .toMongoQuery(false), type)
//                .stream()
//                .map(DomainObject::getId)
//                .collect(Collectors.toList());
//
//        Set<String> parentIds = new LinkedHashSet<>(result);
//
//        // get child publications tree
//        while (!parentIds.isEmpty()) {
//            Params params = new Params()
//                    .addFilters(new InFilter("parentId", parentIds))
//                    .includeFields("_id", "_class");
//
//            params.setSorting(List.of(new Sorting("index", Sort.Direction.ASC)));
//
//            parentIds = mongoOperations.find(params.toMongoQuery(false), type)
//                    .stream()
//                    .map(Publication::getId)
//                    .collect(Collectors.toSet());
//            result.addAll(parentIds);
//        }
        throw new UnsupportedOperationException("Not Yet Implemented");
    }
}


