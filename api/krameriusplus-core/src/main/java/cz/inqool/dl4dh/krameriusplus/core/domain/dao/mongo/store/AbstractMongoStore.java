package cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.store;

import com.querydsl.core.QueryResults;
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
        if (pageable == Pageable.unpaged()) {
            return new QueryResults<>(resultSet, null, null, count);
        }

        return new QueryResults<>(resultSet, (long) pageable.getPageNumber(), pageable.getOffset(), count);
    }
}
