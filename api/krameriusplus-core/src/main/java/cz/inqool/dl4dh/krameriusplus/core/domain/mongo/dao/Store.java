package cz.inqool.dl4dh.krameriusplus.core.domain.mongo.dao;

import cz.inqool.dl4dh.krameriusplus.core.domain.mongo.params.Params;
import lombok.NonNull;

import java.util.Collection;
import java.util.List;

/**
 * Basic interface class for stores. Provides CRUD methods for manipulation with entities in database.
 */
public interface Store<T extends DomainObject> {

    /**
     * create new instance to database
     *
     * @param entity instance to create
     * @return created instance in detached state
     */
    T create(@NonNull T entity);

    /**
     * Saves an instance to database. If entity does not exist, it's created, otherwise it's updated
     * @param entity instance to save
     * @return saved instance
     */
    T save(@NonNull T entity);

    /**
     * Create collection of instances in a batch.
     *
     * @param entities collection of instances to create
     * @return collection of created instances
     */
    Collection<? extends T> create(@NonNull Collection<? extends T> entities);

    /**
     * Saves a collection of instances in a batch. If entity does not exist, it's created, otherwise it's updated
     *
     * @param entities collection of instances to create
     * @return collection of created instances
     */
    Collection<? extends T> save(@NonNull Collection<? extends T> entities);

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
     * Returns the single instance with given ID.
     *
     * @param id ID of instance to be returned
     * @return found instance or {@code null} if not found
     */
    T find(@NonNull String id);

    /**
     * Returns the single instance with given ID. Only the fields listed in includeFields will be included.
     *
     * @param id ID of instance to be returned
     * @param includeFields List of fields to include. If null or empty, all fields will be included
     * @return found instance or {@code null} if not found
     */
    T find(@NonNull String id, List<String> includeFields);

    /**
     * Checks if an object with given ID exists in the database
     * @param id id of object to check
     * @return true if object exists, false otherwise
     */
    boolean exists(@NonNull String id);

    /**
     * Finds all the instances corresponding to the specified list of IDs.
     *
     * @param ids list of IDs
     * @return collection of found instances
     */
    Collection<T> list(@NonNull List<String> ids);

    /**
     * Returns all stored instances. This operation might be very memory consuming.
     *
     * @return
     */
    List<T> listAll();

    /**
     * Returns instances based on defined {@link Params}
     *
     * @param params contains paging, sorting filtering params
     * @return collections of found instances
     */
    List<T> listAll(@NonNull Params params);

    /**
     * Deletes an instance. Non existing instance is silently skipped.
     *
     * @param entity instance to delete
     * @return true if delete was acknowledged, false otherwise
     */
    boolean delete(@NonNull T entity);

    /**
     * Deletes given collection of instances in a batch.
     *
     * @param entities collection of instances to delete
     * @return number of deleted entities
     */
    long delete(@NonNull Collection<? extends T> entities);

}
