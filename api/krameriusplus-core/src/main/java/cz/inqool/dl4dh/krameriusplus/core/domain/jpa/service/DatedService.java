package cz.inqool.dl4dh.krameriusplus.core.domain.jpa.service;

import cz.inqool.dl4dh.krameriusplus.api.domain.DatedObjectCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.domain.DatedObjectDto;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.service.mapper.DatedObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.store.DatedStore;

public interface DatedService<T extends DatedObject, C extends DatedObjectCreateDto, V extends DatedObjectDto>
        extends DomainService<T, C,V> {

    DatedStore<T, ?> getStore();

    DatedObjectMapper<T, C, V> getMapper();

}
