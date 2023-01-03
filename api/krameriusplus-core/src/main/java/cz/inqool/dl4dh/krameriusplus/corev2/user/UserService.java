package cz.inqool.dl4dh.krameriusplus.corev2.user;

import cz.inqool.dl4dh.krameriusplus.api.user.UserDto;
import cz.inqool.dl4dh.krameriusplus.api.user.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserFacade {

    private UserMapper userMapper;

    private UserProvider userProvider;

    @Override
    public UserDto getCurrent() {
        return userMapper.toDto(userProvider.getCurrentUser());
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setUserProvider(UserProvider userProvider) {
        this.userProvider = userProvider;
    }
}
