package cz.inqool.dl4dh.krameriusplus.core.user;

import org.keycloak.KeycloakPrincipal;
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
            userStore.save(user);
        }

        return user;
    }

    private String getUsername(boolean mock) {
        if (mock) {
            return "MOCKED_USER!!!";
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof KeycloakPrincipal) {
            return ((KeycloakPrincipal<?>) principal).getName();
        } else if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            throw new IllegalStateException("Unexpected class of security principal: " + principal.getClass());
        }
    }

    @Autowired
    public void setUserStore(UserStore userStore) {
        this.userStore = userStore;
    }
}
