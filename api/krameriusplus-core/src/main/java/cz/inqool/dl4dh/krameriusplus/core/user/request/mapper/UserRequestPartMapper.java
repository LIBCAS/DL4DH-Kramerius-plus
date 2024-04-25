package cz.inqool.dl4dh.krameriusplus.core.user.request.mapper;

import cz.inqool.dl4dh.krameriusplus.api.user.request.part.UserRequestPartDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserRequestPartMapper {

    UserRequestPartDto toDto(UserRequestPartDto partDto);
}
