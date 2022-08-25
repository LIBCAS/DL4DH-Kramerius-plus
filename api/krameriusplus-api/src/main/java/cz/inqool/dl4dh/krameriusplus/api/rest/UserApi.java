package cz.inqool.dl4dh.krameriusplus.api.rest;

import cz.inqool.dl4dh.krameriusplus.core.system.user.UserService;
import cz.inqool.dl4dh.krameriusplus.core.system.user.dto.AuthRequest;
import cz.inqool.dl4dh.krameriusplus.core.system.user.dto.UserDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.stream.Collectors;

@Tag(name = "User", description = "Užívatelé")
@RestController
@RequestMapping("/api/users")
public class UserApi {

    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid AuthRequest request) {
        try {
            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            userService.login(request)
                    ).build();
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/me")
    public UserDto me(Principal principal, Authentication authentication) {
        UserDto userDto = new UserDto();
        userDto.setUsername(principal.getName());
        userDto.setAuthorities(authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));

        return userDto;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
