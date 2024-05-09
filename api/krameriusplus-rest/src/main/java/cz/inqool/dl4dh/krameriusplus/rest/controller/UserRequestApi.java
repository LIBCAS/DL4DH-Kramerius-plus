package cz.inqool.dl4dh.krameriusplus.rest.controller;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.request.UserRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.request.UserRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.request.UserRequestFacade;
import cz.inqool.dl4dh.krameriusplus.api.request.UserRequestListDto;
import cz.inqool.dl4dh.krameriusplus.api.request.message.MessageCreateDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Arrays;

import static cz.inqool.dl4dh.krameriusplus.api.user.RoleNames.USER;

@Tag(name = "UserRequest", description = "Užívatelské žádosti")
@RestController
@RequestMapping(UserRequestApi.USER_REQUEST_PATH)
@RolesAllowed(USER)
public class UserRequestApi {

    public static final String USER_REQUEST_PATH = "/api/user-requests";

    private final UserRequestFacade userRequestFacade;

    @Autowired
    public UserRequestApi(UserRequestFacade userRequestFacade) {
        this.userRequestFacade = userRequestFacade;
    }

    @PostMapping(value = "/")
    public ResponseEntity<UserRequestDto> createUserRequest(@Valid @RequestBody UserRequestCreateDto createDto,
                                            @RequestParam("files") MultipartFile[] multipartFiles) {
        return new ResponseEntity<>(userRequestFacade
                .createUserRequest(createDto, Arrays.asList(multipartFiles)), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public Result<UserRequestListDto> userRequest(@RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                  @RequestParam(value = "viewDeleted", defaultValue = "false") boolean viewDeleted){
        return userRequestFacade.listPage(Pageable.ofSize(pageSize).withPage(page), viewDeleted);
    }

    @GetMapping("/{requestId}")
    public UserRequestDto findUserRequest(@PathVariable String requestId) {
        return userRequestFacade.findById(requestId);
    }

    @GetMapping("/{requestId}/file/{fileId}")
    public boolean checkFileAccessible(@PathVariable String requestId, @PathVariable String fileId) {
        return userRequestFacade.checkFileAccessible(requestId, fileId);
    }

    @PostMapping("/{requestId}/message")
    public ResponseEntity<Void> createMessage(@PathVariable String requestId,
                                              @Valid @RequestBody MessageCreateDto messageCreateDto,
                                              @RequestParam("files") MultipartFile[] multipartFiles) {
        userRequestFacade.createMessage(requestId, messageCreateDto, Arrays.asList(multipartFiles));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
