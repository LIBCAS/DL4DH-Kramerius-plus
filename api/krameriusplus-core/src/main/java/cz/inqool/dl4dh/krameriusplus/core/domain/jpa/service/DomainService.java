package cz.inqool.dl4dh.krameriusplus.core.domain.jpa.service;

import cz.inqool.dl4dh.krameriusplus.api.domain.DomainObjectCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.domain.DomainObjectDto;
import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.object.DomainObject;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.service.mapper.DomainObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.store.DomainStore;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

public interface DomainService<T extends DomainObject, C extends DomainObjectCreateDto, V extends DomainObjectDto> {

    DomainStore<T, ?> getStore();

    DomainObjectMapper<T, C, V> getMapper();

    /**
     * Find {@link T} in database by id and return it
     *
     * @param id non null, nonempty string in form of UUID
     * @return T object specified by id
     * @throws MissingObjectException if specified T object was not found
     */
    default V find(@NonNull String id) {
        T entity = getStore().findById(id).orElseThrow(() -> new MissingObjectException(getStore().getType(), id));

        return getMapper().toDto(entity);
    }

    /**
     * Find {@link T} in database by id and return it, without mapping it to a DTO
     *
     * @param id non null, nonempty string in form of UUID
     * @return T object specified by id
     * @throws MissingObjectException if specified T object was not found
     */
    default T findEntity(@NonNull String id) {
        return getStore().findById(id).orElseThrow(() -> new MissingObjectException(getStore().getType(), id));
    }

    /**
     * Create new {@link T} by dto
     *
     * @param dto non null, all attributes must be in valid form
     * @return created T object
     */
    @Transactional
    default V create(@NonNull @Valid C dto) {
        return getMapper().toDto(getStore().save(getMapper().fromCreateDto(dto)));
    }

    /**
     * Create new {@link T} by dto
     *
     * @param entity non null, all attributes must be in valid form
     * @return created T object
     */
    @Transactional
    default T create(@NonNull @Valid T entity) {
        return getStore().save(entity);
    }

    /**
     * Update {@link T} by dto
     *
     * @param entity non null, all attributes must be in valid form
     * @return updated T object
     * @throws MissingObjectException if T object was not found
     */
    @Transactional
    default V update(@NonNull @Valid T entity) {
        T entityDb = getStore().findById(entity.getId())
                .orElseThrow(() -> new MissingObjectException(getStore().getType(), entity.getId()));

        T result = getStore().save(entity);
        return getMapper().toDto(result);
    }

    /**
     * Deletes specific T object
     *
     * @param id non null, nonempty string in form of UUID
     */
    @Transactional
    default void delete(@NonNull String id) {
        T entity = getStore().findById(id).orElseThrow(() -> new MissingObjectException(getStore().getType(), id));
        getStore().delete(entity);
    }

}
