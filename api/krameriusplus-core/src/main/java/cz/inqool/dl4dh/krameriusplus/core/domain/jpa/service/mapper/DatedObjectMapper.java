package cz.inqool.dl4dh.krameriusplus.core.domain.jpa.service.mapper;

import cz.inqool.dl4dh.krameriusplus.api.domain.DatedObjectCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.domain.DatedObjectDto;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.object.DatedObject;

public interface DatedObjectMapper<ENTITY extends DatedObject, CDTO extends DatedObjectCreateDto, DTO extends DatedObjectDto>
        extends DomainObjectMapper<ENTITY, CDTO, DTO> {

}
