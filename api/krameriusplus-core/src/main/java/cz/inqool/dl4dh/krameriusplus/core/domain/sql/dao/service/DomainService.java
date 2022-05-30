package cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.service;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.Result;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.object.DomainObject;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.service.dto.DomainObjectCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.service.dto.DomainObjectDto;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.service.mapper.DomainObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.store.DomainStore;
import cz.inqool.dl4dh.krameriusplus.core.utils.Utils;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
//    @PreAuthorize("hasAnyAuthority(this.provide('READ'))")
    @Transactional // only because of loadLazyCollections
    default V find(@NonNull String id) {
        T entity = getStore().find(id);
        Utils.notNull(entity, () -> new MissingObjectException(getStore().getType(), id));

        return getMapper().toDto(entity);
    }

    /**
     * Lists all instances of T object based on Params
     *
     * @param params operation parameters
     * @return list of instances
     */
//    @PreAuthorize("hasAnyAuthority(this.provide('LIST'))")
    @Transactional // only because of loadLazyCollections
    default Result<V> listAll(@NonNull Params params) {
        return getStore().listAll(params).map(getMapper()::toDto);
    }

    /**
     * Lists all instances of T object based on Params (queryDSL)
     *
     * @param params operation parameters
     * @return list of instances
     */
//    @PreAuthorize("hasAnyAuthority(this.provide('LIST'))")
    @Transactional // only because of loadLazyCollections
    default QueryResults<V> list(@NonNull Params params) {
        QueryResults<T> results = getStore().list(params);

        return new QueryResults<V>(results.getResults().stream().map(getMapper()::toDto).collect(Collectors.toList()),
                results.getLimit(),
                results.getOffset(),
                results.getTotal());
    }

    /**
     * Lists all instances of T object
     *
     * @return list of instances
     */
//    @PreAuthorize("hasAnyAuthority(this.provide('LIST'))")
    @Transactional // only because of loadLazyCollections
    default List<V> listAll() {
        return getStore().listAll().stream().map(getMapper()::toDto).collect(Collectors.toList());
    }

    /**
     * Create new {@link T} by dto
     *
     * @param dto non null, all attributes must be in valid form
     * @return created T object
     */
//    @PreAuthorize("hasAnyAuthority(this.provide('WRITE'))")
    @Transactional
    default V create(@NonNull @Valid C dto) {
        return getMapper().toDto(getStore().create(getMapper().fromCreateDto(dto)));
    }

    /**
     * Update {@link T} by dto
     *
     * @param dto non null, all attributes must be in valid form
     * @return updated T object
     * @throws MissingObjectException if T object was not found
     */
//    @PreAuthorize("hasAnyAuthority(this.provide('WRITE'))")
    @Transactional
    default V update(@NonNull @Valid V dto) {
        T entity = getStore().find(dto.getId());
        Utils.notNull(entity, () -> new MissingObjectException(getStore().getType(), dto.getId()));

        entity = getMapper().fromDto(dto);
        T result = getStore().update(entity);
        return getMapper().toDto(result);
    }

    /**
     * Deletes specific T object
     *
     * @param id non null, nonempty string in form of UUID
     */
//    @PreAuthorize("hasAnyAuthority(this.provide('WRITE'))")
    @Transactional
    default void delete(@NonNull String id) {
        T entity = getStore().find(id);
        getStore().delete(entity);
    }

}
