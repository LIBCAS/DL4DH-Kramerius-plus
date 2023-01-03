package cz.inqool.dl4dh.krameriusplus.core.user;

import cz.inqool.dl4dh.krameriusplus.api.user.UserDto;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    UserDto toDto(User currentUser);
}
