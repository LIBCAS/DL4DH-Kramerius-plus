package cz.inqool.dl4dh.krameriusplus.core.security.feeder;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

import static cz.inqool.dl4dh.krameriusplus.core.security.KeycloakSecurityConfigurer.KEYCLOAK_ADMIN_ROLE;

/**
 * System account with user details, which is used by Feeder to authenticate against
 * Kramerius+
 */
public class FeederSystemAccount implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority("ROLE_"+KEYCLOAK_ADMIN_ROLE));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return "FEEDER";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
