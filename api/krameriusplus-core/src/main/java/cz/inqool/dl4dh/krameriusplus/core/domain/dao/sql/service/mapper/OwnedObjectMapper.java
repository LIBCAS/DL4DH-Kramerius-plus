package cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.mapper;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.object.OwnedObject;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.dto.OwnedObjectCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.dto.OwnedObjectDto;

public interface OwnedObjectMapper<ENTITY extends OwnedObject, CDTO extends OwnedObjectCreateDto, DTO extends OwnedObjectDto>
        extends DatedObjectMapper<ENTITY, CDTO, DTO> {
}
