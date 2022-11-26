package cz.inqool.dl4dh.krameriusplus.rest.controller;

import cz.inqool.dl4dh.krameriusplus.api.user.UserDto;
import cz.inqool.dl4dh.krameriusplus.api.user.UserFacade;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "Užívatelé")
@RestController
@RequestMapping("/api/users")
public class UserApi {

    private final UserFacade facade;

    @Autowired
    public UserApi(UserFacade facade) {
        this.facade = facade;
    }

    @GetMapping("/me")
    public UserDto me() {
        return facade.getCurrent();
    }
}
