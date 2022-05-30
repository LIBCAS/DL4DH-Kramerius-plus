package cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.service;


import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.service.dto.DatedObjectCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.service.dto.DatedObjectDto;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.service.mapper.DatedObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.store.DatedStore;

public interface DatedService<T extends DatedObject, C extends DatedObjectCreateDto, V extends DatedObjectDto>
        extends DomainService<T, C,V> {

    DatedStore<T, ?> getStore();

    DatedObjectMapper<T, C, V> getMapper();

}
