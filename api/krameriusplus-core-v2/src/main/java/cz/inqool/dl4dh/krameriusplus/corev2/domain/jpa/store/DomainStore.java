package cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.store;

import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.PersistenceException;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.object.DomainObject;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.api.exception.SystemLogDetails.LogLevel.FATAL_ERROR;
import static cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.PersistenceException.PersistenceErrorCode.QUERY_DSL;
import static java.util.Collections.emptyList;

/**
 * Facade around JPA {@link EntityManager} and QueryDSL providing CRUD operations.
 *
 * <p>
 * All the entity instances should have externally set {@link DomainObject#id} to an {@link UUID}
 * </p>
 *
 * <p>
 * JPA concept of managed/detached instances is prone to development errors. Therefore every instance should be
 * detached upon retrieving. All methods in {@link DomainStore} adhere to this rule.
 * </p>
 *
 * <p>
 * After every saving of instance, the {@link EntityManager}'s context is flushed. This is a rather expensive
 * operation.
 * </p>
 *
 * @param <T> Type of entity to hold
 * @param <Q> Type of query object
 */
public abstract class DomainStore<T extends DomainObject, Q extends EntityPathBase<T>> implements Store<T, Q> {

    /**
     * RDBMS may have limit for bind params count in a query, limit of PGSQL is 32767
     */
    public static final int BIND_PARAMS_LIMIT = 32000;

    /**
     * Entity manager used for JPA
     */
    protected EntityManager entityManager;

    /**
     * QueryDSL query factory
     */
    protected JPAQueryFactory queryFactory;

    /**
     * Entity class object
     */
    @Getter
    protected Class<T> type;

    /**
     * QueryDSL meta class object
     */
    protected Class<Q> qType;

    protected Q qObject;

    public DomainStore(Class<T> type, Class<Q> qType) {
        this.type = type;
        this.qType = qType;

        this.qObject = constructQObject(type, qType);
    }

    /**
     * Checks whether entity with given ID exists in database.
     *
     * @param id ID of entity
     * @return {@code true} if entity exists, {@code false} otherwise
     */
    public boolean exist(@NonNull String id) {
        StringPath idPath = propertyPath("id");

        long count = query().select(idPath)
                .where(idPath.eq(id))
                .fetchCount();

        return count > 0;
    }

    /**
     * create new instance to database
     *
     * @param entity instance to create
     * @return created instance in detached state
     */
    @Override
    public T create(@NonNull T entity) {
        entityManager.persist(entity);

        entityManager.flush();
        detach(entity);

        return entity;
    }

    /**
     * Create collection of instances in a batch.
     *
     * @param entities collection of instances to create
     * @return collection of created instances
     */
    @Override
    public Collection<? extends T> create(@NonNull Collection<? extends T> entities) {
        if (entities.isEmpty()) {
            return emptyList();
        }

        for (T entity : entities) {
            entityManager.persist(entity);
        }

        entityManager.flush();
        detachAll();

        return entities;
    }

    /**
     * Returns the single instance with provided ID.
     *
     * @param id ID of instance to return
     * @return single instance or {@code null} if not found
     */
    @Override
    public T find(@NonNull String id) {
        T entity = entityManager.find(type, id);

        detach(entity);

        return entity;
    }

    /**
     * Finds all the instances corresponding to the specified list of IDs.
     * <p>
     * The returned list of instances is ordered according to the order of provided IDs. If the instance with provided
     * id is not found, it is skipped, therefore the number of returned entities might be of different size that of the
     * provided list of IDs.
     *
     * @param ids                ordered list of IDs
     * @return ordered list of found instances
     * @deprecated try to avoid using lazy collections (use eager collections with/without hibernate views) as they're
     * much slower
     */
    public List<T> list(@NonNull List<String> ids) {
        if (ids.isEmpty()) {
            return emptyList();
        }

        StringPath idPath = propertyPath("id");

        List<T> list = new ArrayList<>();
        for (int i = 0; i < ids.size(); i = i + BIND_PARAMS_LIMIT) {
            List<T> batch = query().select(qObject)
                    .where(idPath.in(ids.subList(i, Math.min(i + BIND_PARAMS_LIMIT, ids.size()))))
                    .fetch();
            list.addAll(batch);
        }

        detachAll();

        return sortByIDs(ids, list);
    }

    @Override
    public T update(@NonNull T entity) {
        T obj = entityManager.merge(entity);

        entityManager.flush();

        detach(obj);

        return obj;
    }

    @Override
    public Collection<? extends T> update(@NonNull Collection<? extends T> entities) {
        if (entities.isEmpty()) {
            return emptyList();
        }

        Set<? extends T> saved = entities.stream()
                .map(entityManager::merge)
                .collect(Collectors.toSet());

        entityManager.flush();

        detachAll();

        return saved;
    }

    @Override
    public T delete(@NonNull T entity) {
        entity = entityManager.find(type, entity.getId());
        if (entity != null) {
            entityManager.remove(entity);
            entityManager.flush();
        }
        return entity;
    }

    @Override
    public Collection<? extends T> delete(@NonNull Collection<? extends T> entities) {
        if (entities.isEmpty()) {
            return emptyList();
        }

        List<? extends T> deletedEntities = entities.stream()
                .filter(Objects::nonNull)
                .map(entity -> entityManager.contains(entity) ? entity : entityManager.find(type, entity.getId()))
                .filter(Objects::nonNull)
                .peek(entity -> entityManager.remove(entity))
                .collect(Collectors.toList());

        entityManager.flush();

        return deletedEntities;
    }

    @Override
    public Long countAll() {
        return query().select(qObject)
                .fetchCount();
    }

    /**
     * Sorts given collection of domain objects by the order specified in the list of their IDs.
     */
    public static <T extends DomainObject> List<T> sortByIDs(List<String> ids, Collection<T> objects) {
        return objects.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(o -> ids.indexOf(o.getId())))
                .collect(Collectors.toList());
    }

    /**
     * Creates QueryDSL query object.
     */
    protected JPAQuery<?> query() {
        return queryFactory.from(qObject);
    }

    protected void detach(T entity) {
        if (entity != null) {
            entityManager.detach(entity);
        }
    }

    protected void detachAll() {
        entityManager.clear();
    }

    /**
     * Creates meta object attribute.
     * <p>
     * Used for addressing QueryDSL attributes, which are not known at compile time. Should be used with caution,
     * because it circumvents type safety.
     *
     * @param name name of the attribute
     * @return meta object attribute
     */
    protected StringPath propertyPath(String name) {
        PathBuilder<T> builder = new PathBuilder<>(qObject.getType(), qObject.getMetadata().getName());
        return builder.getString(name);
    }

    /**
     * Creates meta object attribute.
     * <p>
     * Used for addressing QueryDSL attributes, which are not known at compile time. Should be used with caution,
     * because it circumvents type safety.
     *
     * @param name   name of the attribute
     * @param qClass entity path base class
     * @return meta object attribute
     */
    protected <C extends EntityPathBase<?>> StringPath propertyPath(String name, C qClass) {
        PathBuilder<?> builder = new PathBuilder<>(qClass.getType(), qClass.getMetadata().getName());
        return builder.getString(name);
    }

    private Q constructQObject(Class<T> type, Class<Q> qType) {
        String name = type.getSimpleName();
        name = Character.toLowerCase(name.charAt(0)) + name.substring(1);

        try {
            Constructor<Q> constructor = qType.getConstructor(String.class);
            return constructor.newInstance(name);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new PersistenceException("Error creating Q object for + " + type.getName(), QUERY_DSL, FATAL_ERROR);
        }
    }

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Autowired
    public void setQueryFactory(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

}
