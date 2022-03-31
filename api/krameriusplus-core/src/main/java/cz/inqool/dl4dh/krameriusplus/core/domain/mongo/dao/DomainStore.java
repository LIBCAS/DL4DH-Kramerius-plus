package cz.inqool.dl4dh.krameriusplus.core.domain.mongo.dao;

import com.mongodb.client.result.DeleteResult;
import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.Params;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.util.Streamable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public abstract class DomainStore<T extends DomainObject> implements Store<T> {

    private static final int BATCH_SIZE = 20;

    /**
     * Entity class object
     */
    @Getter
    protected final Class<T> type;

    /**
     * MongoDB driver
     */
    protected final MongoOperations mongoOperations;

    private final EntityInformation<T, String> entityInformation;

    public DomainStore(Class<T> type, MongoOperations mongoOperations) {
        this.type = type;
        this.mongoOperations = mongoOperations;
        this.entityInformation = new MongoRepositoryFactory(mongoOperations).getEntityInformation(type);
    }

    @Override
    public T create(@NonNull T entity) {
        return mongoOperations.insert(entity);
    }

    @Override
    public Collection<? extends T> create(@NonNull Collection<? extends T> entities) {
        if (entities.isEmpty()) {
            return emptyList();
        }

        return insertBatched(entities);
    }

    @Override
    public T save(@NonNull T entity) {
        if (entityInformation.isNew(entity)) {
            return mongoOperations.insert(entity);
        }

        return mongoOperations.save(entity);
    }

    @Override
    public Collection<? extends T> save(@NonNull Collection<? extends T> entities) {
        if (entities.isEmpty()) {
            return emptyList();
        }

        Streamable<? extends T> source = Streamable.of(entities);
        boolean allNew = source.stream().allMatch(entityInformation::isNew);

        if (allNew) {
            return insertBatched(entities);
        }

        return source.stream().map(this::save).collect(Collectors.toList());
    }

    @Override
    public T update(@NonNull T entity) {
        return mongoOperations.save(entity);
    }

    @Override
    public Collection<? extends T> update(@NonNull Collection<? extends T> entities) {
        if (entities.isEmpty()) {
            return emptyList();
        }

        return entities
                .stream()
                .map(this::update)
                .collect(Collectors.toSet());
    }

    @Override
    public T find(@NonNull String id) {
        return mongoOperations.findById(id, type);
    }

    @Override
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

    @Override
    public boolean exists(@NonNull String id) {
        Query query = new Query();
        query.addCriteria(where("id").is(id));

        return mongoOperations.exists(query, type);
    }

    @Override
    public Collection<T> list(@NonNull List<String> ids) {
        if (ids.isEmpty()) {
            return emptyList();
        }

        return mongoOperations.find(query(where("_id").in(ids)), type);
    }

    @Override
    public List<T> listAll() {
        return mongoOperations.findAll(type);
    }

    @Override
    public List<T> listAll(@NonNull Params params) {
        return mongoOperations.find(params.toQuery(), type);
    }

    @Override
    public boolean delete(@NonNull T entity) {
        DeleteResult deleteResult = mongoOperations.remove(entity);

        return deleteResult.wasAcknowledged();
    }

    @Override
    public long delete(@NonNull Collection<? extends T> entities) {
        long removed = 0;

        if (entities.isEmpty()) {
            return removed;
        }

        for (T entity : entities) {
            removed += delete(entity) ? 1 : 0;
        }

        return removed;
    }

    private Collection<? extends T> insertBatched(Collection<? extends T> entities) {
        List<T> result = new ArrayList<>(entities);
        List<T> saved = new ArrayList<>();
        int offset = 0;
        do {
            saved.addAll(mongoOperations.insert(
                    result.subList(offset, Math.min(offset + BATCH_SIZE, result.size()))));
            offset += BATCH_SIZE;
        } while (saved.size() != result.size());

        return new ArrayList<>(saved);
    }
}
