package cz.inqool.dl4dh.krameriusplus.corev2.user;

import cz.inqool.dl4dh.krameriusplus.api.user.UserDto;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    UserDto toDto(User currentUser);
}
