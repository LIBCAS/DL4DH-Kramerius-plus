package cz.inqool.dl4dh.krameriusplus.core.domain.security.feeder;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class FeederAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return new FeederAuthenticationToken(true);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return FeederAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

