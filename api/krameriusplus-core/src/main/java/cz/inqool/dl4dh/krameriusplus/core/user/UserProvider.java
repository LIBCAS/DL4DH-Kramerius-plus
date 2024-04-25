package cz.inqool.dl4dh.krameriusplus.core.user;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Set;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.core.user.UserRole.*;

@Component
@RequestScope
public class UserProvider {

    private UserStore userStore;

    @Value("${system.security.mock}")
    private boolean mock;

    public User getCurrentUser() {
        User user = buildUser(mock);

        User existing = userStore.findUserByUsername(user.getUsername());
        if (existing == null) {
            return userStore.save(user);
        } else {
            existing.setRole(user.getRole());
        }

        return user;
    }

    private User buildUser(boolean mock) {
        User user = new User();

        if (mock) {
            user.setRole(ADMIN);
            user.setUsername( "MOCKED_USER!!!");
            return user;
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof KeycloakPrincipal) {
            KeycloakPrincipal<?> keycloakPrincipal = (KeycloakPrincipal<?>) principal;
            user.setRole(getKeycloakRole(keycloakPrincipal.getKeycloakSecurityContext()));
            user.setUsername(keycloakPrincipal.getName());
        } else if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            user.setRole(getUserDetailsRole(userDetails));
            user.setUsername(userDetails.getUsername());
        } else {
            throw new IllegalStateException("Unexpected class of security principal: " + principal.getClass());
        }

        return user;
    }

    private UserRole getUserDetailsRole(UserDetails userDetails) {
        Set<UserRole> roles = userDetails.getAuthorities().stream()
                .map(authority -> fromRoleName(authority.getAuthority()))
                .collect(Collectors.toSet());
        if (roles.contains(ADMIN)) {
            return ADMIN;
        }

        return USER;
    }

    private UserRole getKeycloakRole(KeycloakSecurityContext keycloakSecurityContext) {
        Set<UserRole> roles = keycloakSecurityContext.getToken().getRealmAccess().getRoles().stream()
                .map(UserRole::fromRoleName)
                .collect(Collectors.toSet());
        if (roles.contains(ADMIN)) {
            return ADMIN;
        }

        return USER;
    }

    @Autowired
    public void setUserStore(UserStore userStore) {
        this.userStore = userStore;
    }
}
