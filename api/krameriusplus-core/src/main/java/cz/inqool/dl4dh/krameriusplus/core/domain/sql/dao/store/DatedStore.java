package cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.store;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.PersistenceException;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.object.DomainObject;
import lombok.NonNull;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.core.domain.exception.SystemLogDetails.LogLevel.WARNING;
import static cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.PersistenceException.PersistenceErrorCode.DELETED_ENTITY;
import static java.util.Collections.emptyList;

/**
 * {@link DomainStore} specialized implementation automatically setting {@link DatedObject#created}, {@link
 * DatedObject#updated} and {@link DatedObject#deleted}.
 *
 * @param <T> Type of entity to hold
 * @param <Q> Type of query object
 */
abstract public class DatedStore<T extends DatedObject, Q extends EntityPathBase<T>> extends DomainStore<T, Q> {

    public DatedStore(Class<T> type, Class<Q> qType) {
        super(type, qType);
    }

    @Override
    public boolean exist(@NonNull String id) {
        long count = query()
                .where(propertyPath("id").eq(id))
                .fetchCount();

        return count > 0;
    }

    @Override
    public T create(@NonNull T entity) {
        entity.setDeleted(null);
        return super.create(entity);
    }

    @Override
    public T update(@NonNull T entity) {
        if (isDeleted(entity.getId())) {
            throw new PersistenceException("Deleted entity can not be updated.", DELETED_ENTITY, WARNING);
        }
        entity.setUpdated(null); // to force hibernate to autogenerate new value
        entity.setDeleted(null);
        return super.update(entity);
    }

    @Override
    public Collection<? extends T> update(@NonNull Collection<? extends T> entities) {
        List<String> entityIDs = entities.stream()
                .map(DomainObject::getId)
                .collect(Collectors.toList());

        if (isAnyDeleted(entityIDs)) {
            throw new PersistenceException("Deleted entities can not be updated.", DELETED_ENTITY, WARNING);
        }
        entities.forEach(entity -> {
            entity.setUpdated(null); // to force hibernate to autogenerate new value
            entity.setDeleted(null);
        });
        return super.update(entities);
    }

    @Override
    public Collection<? extends T> create(@NonNull Collection<? extends T> entities) {
        entities.forEach(entity -> entity.setDeleted(null));
        return super.create(entities);
    }

    @Override
    public Collection<? extends T> delete(@NonNull Collection<? extends T> entities) {
        if (entities.isEmpty()) {
            return emptyList();
        }

        final Instant now = Instant.now();

        List<T> deletedEntities = entities.stream()
                .filter(Objects::nonNull)
                .map(entity -> entityManager.contains(entity) ? entity : entityManager.find(type, entity.getId()))
                .filter(Objects::nonNull)
                .peek(entity -> entity.setDeleted(now))
                .collect(Collectors.toList());

        entityManager.flush();
        detachAll();

        return deletedEntities;
    }

    @Override
    public T delete(@NonNull T entity) {
        final Instant now = Instant.now();

        entity = entityManager.find(type, entity.getId());
        if (entity != null) {
            entity.setDeleted(now);
            entityManager.flush();
        }
        return entity;
    }

    /**
     * Deletes an instance permanently (instead of keeping it with the deleted flag set). Non existing instance is
     * silently skipped.
     *
     * @param entity instance to delete
     * @return resultant entity (with uninitialized lazy collections) or {@code null} if the entity was not found
     */
    public T deletePermanently(@NonNull T entity) {
        return super.delete(entity);
    }

    /**
     * Deletes instances permanently (instead of keeping them with the deleted flag set). Non existing instances are
     * silently skipped.
     *
     * @see #deletePermanently(DatedObject)
     */
    public Collection<? extends T> deletePermanently(@NonNull Collection<? extends T> entities) {
        return super.delete(entities);
    }

    public T restore(@NonNull String id) {
        T entity = entityManager.find(type, id);

        if (entity != null) {
            entity.setDeleted(null);

            entityManager.flush();
            detach(entity);
        }
        return entity;
    }

    public boolean isAnyDeleted(@NonNull List<String> ids) {
        return query()
                .where(propertyPath("id").in(ids))
                .where(propertyPath("deleted").isNotNull())
                .fetchCount() > 0;
    }

    public boolean isDeleted(@NonNull String id) {
        return query()
                .where(propertyPath("id").eq(id))
                .where(propertyPath("deleted").isNotNull())
                .fetchCount() > 0;
    }

    @Override
    protected <C extends EntityPathBase<?>> OrderSpecifier<?>[] defaultListAllOrder(C qClass) {
        return new OrderSpecifier[]{
                propertyPath("created", qClass).asc(),
                propertyPath("id", qClass).asc()
        };
    }

    @Override
    protected void applyWhereExpressions(JPAQuery<T> query) {
        query.where(propertyPath("deleted").isNull());
    }

}
