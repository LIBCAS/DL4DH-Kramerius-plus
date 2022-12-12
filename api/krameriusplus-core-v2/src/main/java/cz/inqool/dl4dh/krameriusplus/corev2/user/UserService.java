package cz.inqool.dl4dh.krameriusplus.corev2.user;

import cz.inqool.dl4dh.krameriusplus.api.user.UserDto;
import cz.inqool.dl4dh.krameriusplus.api.user.UserFacade;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserFacade {
    @Override
    public UserDto getCurrent() {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }
}
