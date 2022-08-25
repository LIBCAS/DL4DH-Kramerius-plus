package cz.inqool.dl4dh.krameriusplus.core.domain.security;

import cz.inqool.dl4dh.krameriusplus.core.domain.security.jwt.JwtFilter;
import cz.inqool.dl4dh.krameriusplus.core.domain.security.jwt.JwtPostFilter;
import cz.inqool.dl4dh.krameriusplus.core.domain.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@Configuration
@ConditionalOnProperty(name = "system.security.auth", havingValue = "jwt")
public class JwtWebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtWebSecurityConfigurer(JwtTokenProvider jwtTokenProvider,
                                    PasswordEncoder passwordEncoder,
                                    UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(jwtTokenProvider);
        auth.userDetailsService(userDetailsService);

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        auth.authenticationProvider(provider);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize -> authorize
                        .antMatchers("/api/info").permitAll()
                        .antMatchers("/h2-console/**").permitAll()
                        .antMatchers("/favicon.ico").permitAll()
                        .antMatchers(HttpMethod.POST, "/api/user/login").permitAll()
                        .anyRequest().authenticated())
                .cors().and()
                .csrf().disable()
                .headers().frameOptions().disable().and()
//                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .exceptionHandling(exceptions ->
                        exceptions.authenticationEntryPoint(((request, response, exception) ->
                                response.sendError(SC_UNAUTHORIZED, exception.getMessage()))));

        http.addFilterBefore(new BasicAuthenticationFilter(authenticationManager()), AnonymousAuthenticationFilter.class);
        http.addFilterBefore(new JwtFilter(), AnonymousAuthenticationFilter.class);
        http.addFilterAfter(new JwtPostFilter(jwtTokenProvider), FilterSecurityInterceptor.class);
    }
}
