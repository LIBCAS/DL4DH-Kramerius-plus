package cz.inqool.dl4dh.krameriusplus.core.system.user;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.DatedService;
import cz.inqool.dl4dh.krameriusplus.core.domain.security.jwt.JwtTokenProvider;
import cz.inqool.dl4dh.krameriusplus.core.system.user.dto.AuthRequest;
import cz.inqool.dl4dh.krameriusplus.core.system.user.dto.UserCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.user.dto.UserDto;
import cz.inqool.dl4dh.krameriusplus.core.system.user.dto.UserMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements DatedService<User, UserCreateDto, UserDto> {

    @Getter
    private UserStore store;

    @Getter
    private UserMapper mapper;

    private PasswordEncoder passwordEncoder;

    private JwtTokenProvider tokenProvider;

    /**
     * Method for login user
     * @param authRequest DTO with user login details
     * @return JWT token if logged in successfully
     */
    public String login(AuthRequest authRequest) {
        String username = authRequest.getUsername();
        User user = store.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username '" + username + "' not found."));

        if (passwordMatches(user, authRequest.getPassword())) {
            return tokenProvider.generateToken(user);
        } else {
            throw new BadCredentialsException("Invalid credentials.");
        }
    }

    private boolean passwordMatches(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    @Autowired
    public void setStore(UserStore store) {
        this.store = store;
    }

    @Autowired
    public void setMapper(UserMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setTokenProvider(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
