package cz.inqool.dl4dh.krameriusplus.rest.controller;

import cz.inqool.dl4dh.krameriusplus.api.request.UserRequestFacade;
import cz.inqool.dl4dh.krameriusplus.api.request.UserRequestState;
import cz.inqool.dl4dh.krameriusplus.api.request.document.ChangeDocumentStatesDto;
import cz.inqool.dl4dh.krameriusplus.api.user.RoleNames;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

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

    @Operation(summary = "Update a user request state")
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

    @Operation(summary = "Update a state of a document in a user request")
    @PutMapping("/{requestId}/document")
    public ResponseEntity<Void> changeDocumentState(@PathVariable String requestId,
                                                    @Valid @RequestBody ChangeDocumentStatesDto dto) {
        if (userRequestFacade.changeDocumentState(requestId, dto.getPublicationIds(), dto.getState(), dto.isForceTransitions())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
