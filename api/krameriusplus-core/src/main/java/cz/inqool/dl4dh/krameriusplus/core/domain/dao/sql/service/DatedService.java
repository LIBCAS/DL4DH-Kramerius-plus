package cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service;


import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.dto.DatedObjectCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.dto.DatedObjectDto;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.mapper.DatedObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.store.DatedStore;

public interface DatedService<T extends DatedObject, C extends DatedObjectCreateDto, V extends DatedObjectDto>
        extends DomainService<T, C,V> {

    DatedStore<T, ?> getStore();

    DatedObjectMapper<T, C, V> getMapper();

}
