package cz.inqool.dl4dh.krameriusplus.core.domain.security.jwt;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.logging.log4j.util.Strings.isEmpty;

public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTHENTICATION_HEADER_NAME = "Bearer";

    @Override
    public void doFilterInternal(HttpServletRequest request,
                                 HttpServletResponse response,
                                 FilterChain chain) throws IOException, ServletException {
        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!isEmpty(header) && header.startsWith(AUTHENTICATION_HEADER_NAME)) {
            String token = extractTokenFromHeader(header);

            Authentication auth = new JwtToken(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }

    private String extractTokenFromHeader(String header) {
        return header.substring(AUTHENTICATION_HEADER_NAME.length() + 1);
    }
}
