package cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.mapper;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.dto.DatedObjectCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.dto.DatedObjectDto;

public interface DatedObjectMapper<ENTITY extends DatedObject, CDTO extends DatedObjectCreateDto, DTO extends DatedObjectDto>
        extends DomainObjectMapper<ENTITY, CDTO, DTO> {

    @IgnoreDatedAttributes
    ENTITY fromDto(DTO dto);

}
