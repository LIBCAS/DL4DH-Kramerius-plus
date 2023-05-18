package cz.inqool.dl4dh.krameriusplus.core.security;

import cz.inqool.dl4dh.krameriusplus.core.security.feeder.FeederAuthenticationFilter;
import cz.inqool.dl4dh.krameriusplus.core.security.feeder.FeederAuthenticationProvider;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@KeycloakConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class KeycloakSecurityConfigurer extends KeycloakWebSecurityConfigurerAdapter {

    public static final String KEYCLOAK_ADMIN_ROLE = "dl4dh-admin";

    @Value("${system.security.secret}")
    private String feederSecret;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        KeycloakAuthenticationProvider provider = keycloakAuthenticationProvider();
        provider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(provider);

        auth.authenticationProvider(new FeederAuthenticationProvider());
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Bean
    @Override
    protected KeycloakAuthenticationProcessingFilter keycloakAuthenticationProcessingFilter() throws Exception {
        RequestMatcher requestMatcher =
                new AndRequestMatcher(
                        KeycloakAuthenticationProcessingFilter.DEFAULT_REQUEST_MATCHER,
                        new IgnoreKeycloakProcessingFilterRequestMatcher()
                );
        return new KeycloakAuthenticationProcessingFilter(authenticationManagerBean(), requestMatcher);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http
                .authorizeRequests(authorize -> authorize
                        .antMatchers("/api/info").permitAll()
                        .antMatchers("/h2-console/**").permitAll()
                        .antMatchers("/favicon.ico").permitAll()
                        .antMatchers("/swagger", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().hasRole(KEYCLOAK_ADMIN_ROLE))
                .cors().and()
                .csrf().disable()
                .headers().frameOptions().disable().and()
                .exceptionHandling(exceptions ->
                        exceptions.authenticationEntryPoint(((request, response, exception) ->
                                response.sendError(SC_UNAUTHORIZED, exception.getMessage()))))
                .addFilterBefore(new FeederAuthenticationFilter(feederSecret), KeycloakPreAuthActionsFilter.class);
    }

    // Matches request with Authorization header which value doesn't start with "Feeder " prefix
    private static class IgnoreKeycloakProcessingFilterRequestMatcher implements RequestMatcher {

        @Override
        public boolean matches(HttpServletRequest request) {
            String authorizationHeaderValue = request.getHeader("Authorization");
            return authorizationHeaderValue != null && !authorizationHeaderValue.startsWith("Feeder ");
        }
    }
}
