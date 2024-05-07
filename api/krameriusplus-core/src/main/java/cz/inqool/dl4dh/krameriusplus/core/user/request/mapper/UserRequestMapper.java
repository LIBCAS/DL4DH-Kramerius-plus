package cz.inqool.dl4dh.krameriusplus.core.user.request.mapper;

import cz.inqool.dl4dh.krameriusplus.api.user.request.UserRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.user.request.UserRequestListDto;
import cz.inqool.dl4dh.krameriusplus.core.user.request.entity.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = UserRequestPartMapper.class)
public interface UserRequestMapper {

    @Mappings({
            @Mapping(source = "user.username", target = "username"),
            @Mapping(source = "requestIdentification", target = "identification")}
    )
    UserRequestDto toDto(UserRequest userRequest);

    @Mappings({
            @Mapping(source = "user.username", target = "username"),
            @Mapping(source = "requestIdentification", target = "identification")}
    )
    UserRequestListDto toListDto(UserRequest userRequest);
}
