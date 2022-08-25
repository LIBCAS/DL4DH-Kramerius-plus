package cz.inqool.dl4dh.krameriusplus.api.rest;

import cz.inqool.dl4dh.krameriusplus.core.domain.security.UserDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.stream.Collectors;

@Tag(name = "User", description = "Užívatelé")
@RestController
@RequestMapping("/api/users")
public class UserApi {

    @GetMapping("/me")
    public UserDto me(Principal principal, Authentication authentication) {
        UserDto userDto = new UserDto();
        userDto.setUsername(principal.getName());
        userDto.setAuthorities(authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));

        return userDto;
    }
}
