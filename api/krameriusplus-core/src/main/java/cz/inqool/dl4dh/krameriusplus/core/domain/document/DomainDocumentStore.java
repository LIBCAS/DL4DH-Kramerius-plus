package cz.inqool.dl4dh.krameriusplus.core.domain.document;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public abstract class DomainDocumentStore<T> {

    protected final MongoOperations mongoOperations;

    protected final Class<T> type;

    public DomainDocumentStore(MongoOperations mongoOperations, Class<T> type) {
        this.mongoOperations = mongoOperations;
        this.type = type;
    }

    public List<T> list(Query query) {
        return mongoOperations.find(query, type);
    }
}
