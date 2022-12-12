package cz.inqool.dl4dh.krameriusplus.corev2.user;

import cz.inqool.dl4dh.krameriusplus.api.user.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User currentUser) {
        UserDto userDto = new UserDto();

        userDto.setUsername(currentUser.getUsername());

        return userDto;
    }
}
