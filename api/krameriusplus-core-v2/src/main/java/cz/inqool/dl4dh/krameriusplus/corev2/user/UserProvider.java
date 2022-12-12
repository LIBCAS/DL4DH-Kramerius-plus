package cz.inqool.dl4dh.krameriusplus.corev2.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
@RequestScope
public class UserProvider {

    private UserStore userStore;

    @Bean
    @RequestScope
    @ConditionalOnProperty(prefix = "system.security", name = "mock", havingValue = "false", matchIfMissing = true)
    public User getCurrentUser() {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = principal.getUsername();

        User user = userStore.findUserByUsername(username);
        if (user == null) {
            user = new User();
            user.setUsername(username);
            userStore.create(user);
        }

        return user;
    }

    @Bean
    @RequestScope
    @ConditionalOnProperty(prefix = "system.security", name = "mock", havingValue = "true")
    public User getCurrentUserMock() {
        User user = new User();
        user.setUsername("MOCKED_USER!!!");
        userStore.create(user);
        return user;
    }

    @Autowired
    public void setUserStore(UserStore userStore) {
        this.userStore = userStore;
    }
}
