package cz.inqool.dl4dh.krameriusplus.core.security.feeder;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

import static cz.inqool.dl4dh.krameriusplus.core.security.KeycloakSecurityConfigurer.KEYCLOAK_ADMIN_ROLE;

public class FeederAuthenticationToken extends AbstractAuthenticationToken {

    public FeederAuthenticationToken(boolean isAuthenticated) {
        super(Set.of(new SimpleGrantedAuthority("ROLE_"+KEYCLOAK_ADMIN_ROLE)));
        setAuthenticated(isAuthenticated);
        setDetails(new FeederSystemAccount());
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return new FeederSystemAccount();
    }
}
