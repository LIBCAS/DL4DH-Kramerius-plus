package cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.service.mapper;

import cz.inqool.dl4dh.krameriusplus.api.domain.DomainObjectCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.domain.DomainObjectDto;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.object.DomainObject;

public interface DomainObjectMapper<ENTITY extends DomainObject, CDTO extends DomainObjectCreateDto, DTO extends DomainObjectDto> {

    ENTITY fromCreateDto(CDTO createDto);

    DTO toDto(ENTITY entity);

}
