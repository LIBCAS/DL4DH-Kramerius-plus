package cz.inqool.dl4dh.krameriusplus.core.domain.security;

import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@KeycloakConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ConditionalOnProperty(name = "system.security.auth", havingValue = "keycloak")
public class KeycloakSecurityConfigurer extends KeycloakWebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        KeycloakAuthenticationProvider provider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider().setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(provider);
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http
                .authorizeRequests(authorize -> authorize
                        .antMatchers("/api/info").permitAll()
                        .antMatchers("/h2-console/**").permitAll()
                        .antMatchers("/favicon.ico").permitAll()
                        .anyRequest().authenticated())
                .cors().and()
                .csrf().disable()
                .headers().frameOptions().disable().and()
                .exceptionHandling(exceptions ->
                        exceptions.authenticationEntryPoint(((request, response, exception) ->
                                response.sendError(SC_UNAUTHORIZED, exception.getMessage()))));
    }
}
