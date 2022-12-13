package cz.inqool.dl4dh.krameriusplus.corev2.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class UserProvider {

    private UserStore userStore;

    @Value("${system.security.mock}")
    private boolean mock;

    public User getCurrentUser() {
        String username = getUsername(mock);

        User user = userStore.findUserByUsername(username);
        if (user == null) {
            user = new User();
            user.setUsername(username);
            userStore.create(user);
        }

        return user;
    }

    private String getUsername(boolean mock) {
        if (mock) {
            return "MOCKED_USER!!!";
        }

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return principal.getUsername();
    }

    @Autowired
    public void setUserStore(UserStore userStore) {
        this.userStore = userStore;
    }
}
