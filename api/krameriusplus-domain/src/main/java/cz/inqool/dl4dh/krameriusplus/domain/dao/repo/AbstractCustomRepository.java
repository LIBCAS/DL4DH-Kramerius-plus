package cz.inqool.dl4dh.krameriusplus.domain.dao.repo;

import cz.inqool.dl4dh.krameriusplus.domain.dao.params.Params;
import cz.inqool.dl4dh.krameriusplus.domain.dao.params.filter.Filter;
import cz.inqool.dl4dh.krameriusplus.domain.entity.DomainObject;
import lombok.NonNull;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class AbstractCustomRepository<T extends DomainObject> implements CustomRepository<T> {

    private final MongoTemplate mongoTemplate;

    private final Class<T> clazz;

    public AbstractCustomRepository(Class<T> clazz, MongoTemplate mongoTemplate) {
        this.clazz = clazz;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<T> list(@NonNull Params params) {
        Query query = new Query();

        for (Filter filter : params.getFilters()) {
            query.addCriteria(filter.toCriteria());
        }

        for (String field : params.getIncludeFields()) {
            query.fields().include(field);
        }

        for (String field : params.getExcludeFields()) {
            query.fields().exclude(field);
        }

        query.fields().include("_class"); // always include _class so the DB object can be deserialized
        query.with(params.toPageRequest());

        return mongoTemplate.find(query, clazz);
    }
}
