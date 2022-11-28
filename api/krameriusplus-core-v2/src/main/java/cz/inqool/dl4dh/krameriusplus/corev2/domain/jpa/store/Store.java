package cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.store;

import com.querydsl.core.types.dsl.EntityPathBase;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.object.DomainObject;
import lombok.NonNull;

import java.util.Collection;
import java.util.List;

/**
 * Basic interface class for stores. Provides CRUD methods for manipulation with entities in database.
 */
public interface Store<T extends DomainObject, Q extends EntityPathBase<T>> {

    /**
     * Returns the single instance with given ID.
     *
     * @param id ID of instance to be returned
     * @return found instance or {@code null} if not found
     */
    T find(@NonNull String id);

    /**
     * Finds all the instances corresponding to the specified list of IDs.
     *
     * @param ids list of IDs
     * @return collection of found instances
     */
    Collection<T> list(@NonNull List<String> ids);

    /**
     * create new instance to database
     *
     * @param entity instance to create
     * @return created instance in detached state
     */
    T create(@NonNull T entity);

    /**
     * Create collection of instances in a batch.
     *
     * @param entities collection of instances to create
     * @return collection of created instances
     */
    Collection<? extends T> create(@NonNull Collection<? extends T> entities);

    /**
     * Update given instance.
     *
     * @param entity instance to update
     * @return saved instance
     */
    T update(@NonNull T entity);

    /**
     * Update given collection of instances in a batch.
     *
     * @param entities collection of instances to update
     * @return collection of updated instances
     */
    Collection<? extends T> update(@NonNull Collection<? extends T> entities);

    /**
     * Deletes an instance. Non existing instance is silently skipped.
     *
     * @param entity instance to delete
     * @return resultant entity (with uninitialized lazy collections) or {@code null} if the entity was not found
     */
    T delete(@NonNull T entity);

    /**
     * Deletes given collection of instances in a batch.
     *
     * @param entities collection of instances to delete
     * @return collection of deleted instances
     */
    Collection<? extends T> delete(@NonNull Collection<? extends T> entities);

    /**
     * Returns the number of stored instances.
     */
    Long countAll();

}
