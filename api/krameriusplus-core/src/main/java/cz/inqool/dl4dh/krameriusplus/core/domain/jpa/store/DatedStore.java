package cz.inqool.dl4dh.krameriusplus.core.domain.jpa.store;

import com.querydsl.core.types.dsl.EntityPathBase;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.object.DatedObject;
import lombok.NonNull;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.time.Instant;

/**
 * {@link DomainStore} specialized implementation automatically setting {@link DatedObject#created}, {@link
 * DatedObject#updated} and {@link DatedObject#deleted}.
 *
 * @param <T> Type of entity to hold
 * @param <Q> Type of query object
 */
abstract public class DatedStore<T extends DatedObject, Q extends EntityPathBase<T>> extends DomainStore<T, Q> {

    public DatedStore(Class<T> type, Class<Q> qType, EntityManager em) {
        super(type, qType, em);
    }

    @Override
    public void delete(@NonNull T entity) {
        Assert.notNull(entity, "Entity must not be null!");

        entity = entityManager.find(type, entity.getId());
        if (entity != null) {
            final Instant now = Instant.now();
            entity.setDeleted(now);
        }
    }

    /**
     * Deletes an instance permanently (instead of keeping it with the deleted flag set). Non existing instance is
     * silently skipped.
     *
     * @param entity instance to delete
     * @return resultant entity (with uninitialized lazy collections) or {@code null} if the entity was not found
     */
    public void deletePermanently(@NonNull T entity) {
        super.delete(entity);
    }

    public T restore(@NonNull String id) {
        T entity = entityManager.find(type, id);

        if (entity != null) {
            entity.setDeleted(null);
        }

        return entity;
    }
}
