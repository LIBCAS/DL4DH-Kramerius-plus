package cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.store;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Params;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.List;

public abstract class AbstractMongoStore<T> {

    protected final MongoOperations mongoOperations;

    protected final Class<T> type;

    public AbstractMongoStore(MongoOperations mongoOperations, Class<T> type) {
        this.mongoOperations = mongoOperations;
        this.type = type;
    }

    protected QueryResults<T> constructQueryResults(List<T> resultSet, Pageable pageable, long count) {
        return new QueryResults<>(resultSet, pageable, count);
    }

    public QueryResults<T> findAll(Params params) {
        long total = mongoOperations.count(params.toMongoQuery(false), type);

        List<T> result = mongoOperations.find(params.toMongoQuery(), type);

        return constructQueryResults(result, params.toPageable(), total);
    }
}
