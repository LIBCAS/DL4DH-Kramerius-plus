package cz.inqool.dl4dh.krameriusplus.rest.controller;

import cz.inqool.dl4dh.krameriusplus.api.user.RoleNames;
import cz.inqool.dl4dh.krameriusplus.api.user.request.UserRequestFacade;
import cz.inqool.dl4dh.krameriusplus.api.user.request.UserRequestState;
import cz.inqool.dl4dh.krameriusplus.api.user.request.document.DocumentState;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
@Tag(name = "UserRequestAdmin", description = "Užívatelské žádosti administrace")
@RestController
@RequestMapping(UserRequestAdminApi.USER_REQUEST_ADMIN_PATH)
@RolesAllowed(RoleNames.ADMIN)
public class UserRequestAdminApi {

    public static final String USER_REQUEST_ADMIN_PATH = "/api/user-requests/admin";

    private final UserRequestFacade userRequestFacade;

    @Autowired
    public UserRequestAdminApi(UserRequestFacade userRequestFacade) {
        this.userRequestFacade = userRequestFacade;
    }


    @PutMapping("/{requestId}")
    public ResponseEntity<Void> changeState(@PathVariable String requestId,
                                            @RequestParam UserRequestState state,
                                            @RequestParam(defaultValue = "false") boolean forceTransition) {
        if (userRequestFacade.changeRequestState(requestId, state, forceTransition)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{requestId}/document/{partId}")
    public ResponseEntity<Void> changeDocumentState(@PathVariable String requestId,
                                                    @PathVariable String partId,
                                                    @RequestParam DocumentState state,
                                                    @RequestParam(defaultValue = "false") boolean forceTransition) {
        if (userRequestFacade.changeDocumentState(requestId, partId, state, forceTransition)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
