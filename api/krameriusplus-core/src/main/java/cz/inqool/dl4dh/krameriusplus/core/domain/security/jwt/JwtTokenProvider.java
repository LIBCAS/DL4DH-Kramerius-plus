package cz.inqool.dl4dh.krameriusplus.core.domain.security.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * Spring {@link AuthenticationProvider} implementation based on JWT tokens.
 *
 * <p>
 *     Encapsulates parsing, validation and generating of {@link JwtToken}.
 * </p>
 */
@Component
public class JwtTokenProvider implements AuthenticationProvider {

    private String secret;

    private Long expiration;

    private Long refresh;

    private JwtHandler handler;

    @Override
    public JwtToken authenticate(Authentication authentication) throws AuthenticationException {
        JwtToken token = (JwtToken) authentication;

        if (token.getPrincipal() instanceof String) {

            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(secret)
                        .parseClaimsJws((String) token.getPrincipal())
                        .getBody();

                UserDetails user = handler.parseClaims(claims);

                return new JwtToken(user, claims, user.getAuthorities());
            } catch (SignatureException | ClaimJwtException ex) {
                throw new BadCredentialsException("Cannot authenticate user, JWT token error", ex);
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtToken.class.isAssignableFrom(authentication);
    }

    public String generateToken(UserDetails user) {
        Map<String, Object> claims = handler.createClaims(user);

        Instant now = Instant.now();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(expiration)))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * Generates {@link JwtToken} serialized into {@link String} if the provided {@link JwtToken} is not fresh enough.
     *
     * <p>
     *     Freshness of {@link JwtToken} is based on difference between token creation date and configuration value
     *     for freshness.
     * </p>
     * @param jwt Provided {@link JwtToken}
     * @return newly created serialized token or null if not necessary
     */
    public String refreshIfNeeded(JwtToken jwt) {
        Date issuedAt = jwt.getClaims().getIssuedAt();

        Instant refreshAt = Instant.ofEpochMilli(issuedAt.getTime()).plusSeconds(refresh);
        Instant now = Instant.now();

        if (refreshAt.isBefore(now)) {
            return generateToken(jwt.getDetails());
        } else {
            return null;
        }
    }

    /**
     * Sets the secret used for token signing.
     *
     * @param secret Provided secret
     */
    @Autowired
    public void setSecret(@Value("${system.security.jwt.secret}") String secret) {
        this.secret = secret;
    }

    /**
     * Sets the token validity duration
     * @param expiration Provided validity in seconds
     */
    @Autowired
    public void setExpiration(@Value("${system.security.jwt.expiration:300}") Long expiration) {
        this.expiration = expiration;
    }

    /**
     * Sets the token fresh duration
     * @param refresh Provided fresh in seconds
     */
    @Autowired
    public void setRefresh(@Value("${core.auth.jwt.refresh:30}") Long refresh) {
        this.refresh = refresh;
    }

    @Autowired
    public void setHandler(JwtHandler handler) {
        this.handler = handler;
    }
}
