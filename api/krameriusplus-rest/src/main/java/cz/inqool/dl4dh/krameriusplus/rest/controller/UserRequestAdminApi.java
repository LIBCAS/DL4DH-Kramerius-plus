package cz.inqool.dl4dh.krameriusplus.rest.controller;

import cz.inqool.dl4dh.krameriusplus.api.user.request.UserRequestListDto;
import cz.inqool.dl4dh.krameriusplus.api.user.request.UserRequestState;
import cz.inqool.dl4dh.krameriusplus.api.user.request.document.DocumentState;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
@Tag(name = "UserRequestAdmin", description = "Užívatelské žádosti administrace")
@RestController
@RequestMapping(UserRequestAdminApi.USER_REQUEST_ADMIN_PATH)
@RolesAllowed("dl4dh-admin")
public class UserRequestAdminApi {

    public static final String USER_REQUEST_ADMIN_PATH = "/api/user-requests/admin";

    @GetMapping("/")
    public Page<UserRequestListDto> userRequest(@RequestBody Pageable pageable){
        throw new NotImplementedException();
    }

    @PutMapping("/{requestId}")
    public void changeState(@PathVariable String requestId, @RequestParam UserRequestState state) {
        throw new NotImplementedException();
    }

    @PutMapping("/{requestId}/document/{documentId}")
    public void changeDocumentState(@PathVariable String requestId, @PathVariable String documentId, @RequestParam DocumentState state) {
        throw new NotImplementedException();
    }
}
