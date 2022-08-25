package cz.inqool.dl4dh.krameriusplus.core.domain.security.jwt;

import cz.inqool.dl4dh.krameriusplus.core.system.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class JwtHandlerImpl implements JwtHandler {

    private final UserDetailsService detailService;

    @Autowired
    public JwtHandlerImpl(UserDetailsService detailService) {
        this.detailService = detailService;
    }

    public UserDetails parseClaims(Map<String, Object> claims) {
        String username = (String) claims.get("username");

        return detailService.loadUserByUsername(username);
    }

    public Map<String, Object> createClaims(UserDetails userDetails) {
        if (userDetails instanceof User) {
            User user = (User) userDetails;

            Map<String, Object> claims = new LinkedHashMap<>();
            claims.put("firstName", user.getFirstName());
            claims.put("lastName", user.getLastName());
            claims.put("username", user.getUsername());

            return claims;
        } else {
            throw new IllegalArgumentException("UserDetails is not instance of UserIdentity");
        }
    }

}
