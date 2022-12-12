package cz.inqool.dl4dh.krameriusplus.corev2.user;

import cz.inqool.dl4dh.krameriusplus.api.user.UserDto;
import cz.inqool.dl4dh.krameriusplus.api.user.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserFacade {

    private UserMapper userMapper;

    private User currentUser;

    @Override
    public UserDto getCurrent() {
        return userMapper.toDto(currentUser);
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
