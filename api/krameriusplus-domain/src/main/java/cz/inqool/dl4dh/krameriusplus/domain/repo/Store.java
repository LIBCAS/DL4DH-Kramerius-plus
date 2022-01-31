package cz.inqool.dl4dh.krameriusplus.domain.repo;

import cz.inqool.dl4dh.krameriusplus.domain.entity.DomainObject;
import cz.inqool.dl4dh.krameriusplus.domain.params.Params;
import cz.inqool.dl4dh.krameriusplus.domain.params.filter.Filter;
import lombok.NonNull;

import java.util.Collection;
import java.util.List;

/**
 * Basic interface class for stores. Provides CRUD methods for manipulation with entities in database.
 */
public interface Store<T extends DomainObject> {

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
     * Returns all stored instances.
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
     * Returns instances in pages/batches.
     *
     * @param page     defines the page for the query results
     * @param pageSize defines the limit or results per page
     * @return collection of found instances
     */
    List<T> listAll(int page, int pageSize, List<Filter> filters);

    /**
     * Returns instances in pages/batches.
     *
     * @param page                defines the page for the query results
     * @param pageSize            defines the limit or results per page
     * @param loadLazyCollections boolean if lazy collections shoud be loaded
     * @return collection of found instances
     */
    List<T> listAll(int page, int pageSize, List<Filter> filters, boolean loadLazyCollections);

    /**
     * Returns all instances with sorting
     *
     * @param sortByAttribute     name of the attribute by which we are sorting
     * @param order               order of sorting - ascending or descending
     * @param loadLazyCollections boolean if lazy collections shoud be loaded
     * @return collection of found instances
     */
//    List<T> listAllSorted(String sortByAttribute, Order order, List<Filter> filters, boolean loadLazyCollections);

    /**
     * Returns instances in batches.
     *
     * @param sortByAttribute     name of the attribute by which we are sorting
     * @param order               order of sorting - ascending or descending
     * @param page                defines the page for the query results
     * @param pageSize            defines the limit or results per page
     * @param loadLazyCollections boolean if lazy collections shoud be loaded
     * @return collection of found instances
     */
//    List<T> listAllSorted(String sortByAttribute, Order order, int page, int pageSize, List<Filter> filters, boolean loadLazyCollections);

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
