package cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.store;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.PersistenceException;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.Result;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.object.DomainObject;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.params.Filter;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.params.Paging;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.params.Sorting;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.data.querydsl.QSort;

import javax.persistence.EntityManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.core.domain.mongo.exception.SystemLogDetails.LogLevel.FATAL_ERROR;
import static cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.PersistenceException.PersistenceErrorCode.QUERY_DSL;
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
        return find(id, true);
    }

    /**
     * Returns the single instance with provided ID.
     *
     * @param id                 ID of instance to return
     * @param loadLazyCollection Indicates if lazy collections should be loaded
     * @return single instance or {@code null} if not found
     * @deprecated try to avoid using lazy collections (use eager collections with/without hibernate views) as they're
     * much slower
     */
    public T find(@NonNull String id, boolean loadLazyCollection) {
        StringPath idPath = propertyPath("id");

        JPAQuery<T> query = query().select(qObject)
                .where(idPath.eq(id));

        this.applyWhereExpressions(query);

        T entity = query.fetchFirst();

        if (entity != null) {
            if (loadLazyCollection) {
                loadLazyCollections(entity);
            }
            detachAll();
        }

        return entity;
    }

    public QueryResults<T> list(Params params) {
        JPAQuery<T> query = query().select(qObject).from(qObject);

        if (params.getFilters() != null) {
            params.getFilters().forEach(filter -> this.applyFilter(query, filter));
        }

        OrderSpecifier<?>[] orderSpecifier;
        if (params.getSorting() != null) {
            String sortByAttribute = params.getSorting().getField();
            if (Order.DESC.equals(params.getSorting().getSort().query())) {
                orderSpecifier = getOrderSpecifierDescending(qObject, sortByAttribute);
            } else {
                orderSpecifier = getOrderSpecifierAscending(qObject, sortByAttribute);
            }
        } else {
            orderSpecifier = defaultListAllOrder(qObject);
        }

        query.orderBy(orderSpecifier);

        if (params.getPaging() != null) {
            query.offset((long) params.getPaging().getPage() * params.getPaging().getPageSize());
            query.limit(params.getPaging().getPageSize());
        }

        QueryResults<T> result = query.fetchResults();

        detachAll();

        return result;
    }

    /**
     * Finds all the instances corresponding to the specified list of IDs.
     * <p>
     * The returned list of instances is ordered according to the order of provided IDs. If the instance with provided
     * id is not found, it is skipped, therefore the number of returned entities might be of different size that of the
     * provided list of IDs.
     * <p>
     * Lazy collection won't be loaded.
     *
     * @param ids ordered list of IDs
     * @return ordered list of found instances
     */
    @Override
    public List<T> list(@NonNull List<String> ids) {
        return list(ids, false);
    }

    /**
     * Finds all the instances corresponding to the specified list of IDs.
     * <p>
     * The returned list of instances is ordered according to the order of provided IDs. If the instance with provided
     * id is not found, it is skipped, therefore the number of returned entities might be of different size that of the
     * provided list of IDs.
     *
     * @param ids                ordered list of IDs
     * @param loadLazyCollection Indicates if lazy collections should be loaded
     * @return ordered list of found instances
     * @deprecated try to avoid using lazy collections (use eager collections with/without hibernate views) as they're
     * much slower
     */
    public List<T> list(@NonNull List<String> ids, boolean loadLazyCollection) {
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

        if (loadLazyCollection) {
            list.forEach(this::loadLazyCollections);
        }

        detachAll();

        return sortByIDs(ids, list);
    }

    /**
     * Returns all stored instances.
     * <p>
     * Possibly very costly operation. Should be used only if we know there is not many instances or for debugging
     * purposes.
     */
    @Override
    public List<T> listAll() {
        int pageSize = countAll().intValue();
        return listAll(0, (pageSize <= 0) ? 1 : pageSize, List.of(), true).getContent(); // page size cannot be less than 1
    }

    @Override
    public Result<T> listAll(@NonNull Params params) {
        Paging paging = params.getPaging();
        Sorting sorting = params.getSorting();
        List<Filter> filters = params.getFilters();

        if (paging == null && sorting == null) {
            int pageSize = countAll().intValue();
            return listAll(0, (pageSize <= 0) ? 1 : pageSize, filters, true);
        } else if (paging != null && sorting == null) {
            return listAll(paging.getPage(), paging.getPageSize(), filters, params.isLazyLoading());
        } else if (paging == null && sorting != null) {
            return listAllSorted(sorting.getField(), sorting.getSort().query(), filters, params.isLazyLoading());
        } else {
            return listAllSorted(sorting.getField(), sorting.getSort().query(), paging.getPage(), paging.getPageSize(), filters, params.isLazyLoading());
        }
    }

    @Override
    public Result<T> listAll(int page, int pageSize, List<Filter> filters) {
        return listAll(page, pageSize, filters, false);
    }

    @Override
    public Result<T> listAll(int page, int pageSize, List<Filter> filters, boolean loadLazyCollections) {
        return listAllSorted(null, Order.ASC, page, pageSize, filters, loadLazyCollections);
    }

    @Override
    public Result<T> listAllSorted(String sortByAttribute, Order order, List<Filter> filters, boolean loadLazyCollection) {
        return listAllSorted(sortByAttribute, order, 0, countAll().intValue(), filters, loadLazyCollection);
    }

    @Override
    public Result<T> listAllSorted(String sortByAttribute, Order order, int page, int pageSize, List<Filter> filters, boolean loadLazyCollection) {
        JPAQuery<T> query = query().select(qObject).from(qObject);

        if (filters != null) {
            filters.forEach(filter -> this.applyFilter(query, filter));
        }

        this.applyWhereExpressions(query);

        OrderSpecifier<?>[] orderSpecifier;
        if (sortByAttribute != null) {
            if (Order.DESC.equals(order)) {
                orderSpecifier = getOrderSpecifierDescending(qObject, sortByAttribute);
            } else {
                orderSpecifier = getOrderSpecifierAscending(qObject, sortByAttribute);
            }
        } else {
            orderSpecifier = defaultListAllOrder(qObject);
        }

        QPageRequest pageable = QPageRequest.of(page, pageSize, QSort.by(orderSpecifier));
        List<T> queryResult = querydsl().applyPagination(pageable, query).fetch();

        Page<T> result = new PageImpl<T>(queryResult, pageable, query.fetchCount());

        if (loadLazyCollection) {
            result.getContent().forEach(this::loadLazyCollections);
        }

        detachAll();

        return new Result<>(result);
    }

    @Override
    public T update(@NonNull T entity) {
        T obj = entityManager.merge(entity);

        entityManager.flush();

        loadLazyCollections(obj); // todo: find better way, fixed lazy loading for collections with bidirectional relationship
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

        saved.forEach(this::loadLazyCollections); // todo: find better way, fixed lazy loading for collections with bidirectional relationship
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

    protected Querydsl querydsl() {
        return new Querydsl(entityManager, (new PathBuilderFactory()).create(qType));
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

    protected <C extends EntityPathBase<?>> OrderSpecifier<?>[] defaultListAllOrder(C qClass) {
        return new OrderSpecifier[]{propertyPath("id", qClass).asc()};
    }

    protected <C extends EntityPathBase<?>> OrderSpecifier<?>[] getOrderSpecifierAscending(C qClass, String attribute) {
        return new OrderSpecifier[]{propertyPath(attribute, qClass).asc()};
    }

    protected <C extends EntityPathBase<?>> OrderSpecifier<?>[] getOrderSpecifierDescending(C qClass, String attribute) {
        return new OrderSpecifier[]{propertyPath(attribute, qClass).desc()};
    }

    /**
     * Initializes lazy collections in single entity find retrieval.
     * <p>
     * Need to override in specific stores.
     *
     * @param entity Entity to load
     * @deprecated try to avoid using lazy collections (use eager collections with/without hibernate views) as they're
     * much slower
     */
    protected void loadLazyCollections(T entity) {
        // do nothing
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

    @SneakyThrows
    protected void applyFilter(JPAQuery<T> query, Filter filter) {
        Field field = this.resolveField(type, filter.getField());
        Expression<?> path = this.resolvePath(filter.getField());

        Expression<?> valueExpression;
        if (Enum.class.isAssignableFrom(field.getType())) {
            valueExpression = Expressions.constant(Enum.valueOf((Class<Enum>) field.getType(), filter.getValue()));
        } else {
            valueExpression = Expressions.constant(filter.getValue());
        }
        query.where(Expressions.predicate(
                filter.getOperator().ops(),
                path,
                valueExpression));
    }

    @SneakyThrows
    private Expression<?> resolvePath(String fieldName) {
        for (Field field : qType.getDeclaredFields()) {
            if (Expression.class.isAssignableFrom(field.getType()) && field.getName().equals(fieldName)) {
                return (Expression<?>) field.get(qObject);
            }
        }

        throw new IllegalStateException("No field of " + qType.getName() + " matches field " + fieldName);
    }

    @SneakyThrows
    private Field resolveField(Class<?> clazz, String fieldName) {
        if (clazz == Object.class) {
            throw new IllegalArgumentException("No field with name " + fieldName + " found on given object");
        }

        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }

        return resolveField(clazz.getSuperclass(), fieldName);
    }

    protected void applyWhereExpressions(JPAQuery<T> query) {
        //do nothing in domain store
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
