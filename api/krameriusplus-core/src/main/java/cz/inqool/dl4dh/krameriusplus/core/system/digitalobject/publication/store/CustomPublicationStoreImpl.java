package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.store;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.store.AbstractMongoStore;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.dto.PublicationListFilterDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class CustomPublicationStoreImpl extends AbstractMongoStore<Publication> implements CustomPublicationStore {

    public CustomPublicationStoreImpl(MongoOperations mongoOperations) {
        super(mongoOperations, Publication.class);
    }

    public QueryResults<Publication> list(String publicationId, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "index"));

        Query query = Query.query(where("parentId").is(publicationId));

        long total = mongoOperations.count(query, type);

        return new QueryResults<>(mongoOperations.find(query.with(pageRequest), type), (long) pageSize, (long) page * pageSize, total);
    }

    public QueryResults<Publication> list(PublicationListFilterDto filter, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "created"));

        Query query = new Query();

        if (filter.getTitle() != null) {
            query.addCriteria(where("title").regex(filter.getTitle()));
        }

        if (filter.getParentId() != null) {
            query.addCriteria(where("parentId").is(filter.getParentId()));
        }

        if (filter.getCreatedBefore() != null) {
            query.addCriteria(where("created").lte(filter.getCreatedBefore()));
        }

        if (filter.getCreatedAfter() != null) {
            query.addCriteria(where("created").gte(filter.getCreatedAfter()));
        }

        if (filter.getIsPublished() != null) {
            query.addCriteria(where("publishInfo.isPublished").is(filter.getIsPublished()));
        }

        if (filter.getPublishedBefore() != null) {
            query.addCriteria(where("publishInfo.publishedLastModified").lte(filter.getPublishedBefore()));
        }

        if (filter.getPublishedAfter() != null) {
            query.addCriteria(where("publishInfo.publishedLastModified").gte(filter.getPublishedAfter()));
        }

        long total = mongoOperations.count(query, type);

        return new QueryResults<>(mongoOperations.find(query.with(pageRequest), type), (long) pageSize, (long) page * pageSize, total);
    }

    public Publication listWithTei(String publicationId) {
        Query query = Query.query(where("_id").is(publicationId));

        query.fields().include("_class", "_id", "teiHeaderFileId");

        return mongoOperations.findOne(query, type);
    }

    public String getTitle(String publicationId) {
        Query query = Query.query(where("_id").is(publicationId));

        query.fields().include("_class", "title");

        Publication publication = mongoOperations.findOne(query, type);
        notNull(publication, () -> new MissingObjectException(Publication.class, publicationId));

        return publication.getTitle();
    }

    public List<Publication> listPublishedModified(Instant publishedModifiedAfter) {
        Query query = Query.query(where("publishInfo.publishedLastModified").gte(publishedModifiedAfter));

        return mongoOperations.find(query, type);
    }
}
