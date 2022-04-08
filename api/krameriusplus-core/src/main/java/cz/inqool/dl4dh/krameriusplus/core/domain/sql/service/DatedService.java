package cz.inqool.dl4dh.krameriusplus.core.domain.sql.service;


import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.store.DatedStore;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.service.dto.DatedObjectCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.service.dto.DatedObjectDto;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.service.mapper.DatedObjectMapper;

public interface DatedService<T extends DatedObject, C extends DatedObjectCreateDto, V extends DatedObjectDto>
        extends DomainService<T, C,V> {

    DatedStore<T, ?> getStore();

    DatedObjectMapper<T, C, V> getMapper();

}
