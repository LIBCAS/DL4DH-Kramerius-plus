package cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.store;

import com.querydsl.core.QueryResults;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public abstract class AbstractMongoStore<T> {

    protected final MongoOperations mongoOperations;

    protected final Class<T> type;

    public AbstractMongoStore(MongoOperations mongoOperations, Class<T> type) {
        this.mongoOperations = mongoOperations;
        this.type = type;
    }

    public T find(@NonNull String id) {
        return mongoOperations.findById(id, type);
    }

    public T find(@NonNull String id, List<String> includeFields) {
        if (includeFields == null || includeFields.isEmpty()) {
            return find(id);
        }

        Query query = new Query();

        for (String field : includeFields) {
            query.fields().include(field);
        }

        query.fields().include("_class"); // always include class so MongoDB can deserialize document into POJO

        query.addCriteria(where("_id").is(id));

        return mongoOperations.findOne(query, type);
    }

    protected QueryResults<T> constructQueryResults(List<T> resultSet, Pageable pageable, long count) {
        if (pageable == Pageable.unpaged()) {
            return new QueryResults<>(resultSet, null, null, count);
        }

        return new QueryResults<>(resultSet, (long) pageable.getPageNumber(), pageable.getOffset(), count);
    }
}
