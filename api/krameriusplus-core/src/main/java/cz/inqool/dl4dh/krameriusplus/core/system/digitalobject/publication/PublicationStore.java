package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.store.DomainStore;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.MissingObjectException;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PublicationStore extends DomainStore<Publication> {

    @Autowired
    public PublicationStore(MongoOperations mongoOperations) {
        super(Publication.class, mongoOperations);
    }

    public QueryResults<Publication> list(String publicationId, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "index"));

        Query query = Query.query(where("parentId").is(publicationId));

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
