package cz.inqool.dl4dh.krameriusplus.corev2.domain.document;

import cz.inqool.dl4dh.krameriusplus.api.Result;
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

    public Result<T> list(Query query) {
        long total = mongoOperations.count(query, type);

        List<T> result = mongoOperations.find(query, type);

        long pageSize = query.getLimit();
        long page = query.getSkip() / query.getLimit();

        return new Result<>(pageSize, page, total, result);
    }
}
