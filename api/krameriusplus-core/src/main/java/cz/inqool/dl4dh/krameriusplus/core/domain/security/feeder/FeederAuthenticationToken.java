package cz.inqool.dl4dh.krameriusplus.core.domain.security.feeder;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.HashSet;

public class FeederAuthenticationToken extends AbstractAuthenticationToken {

    public FeederAuthenticationToken(boolean isAuthenticated) {
        super(new HashSet<>());
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
