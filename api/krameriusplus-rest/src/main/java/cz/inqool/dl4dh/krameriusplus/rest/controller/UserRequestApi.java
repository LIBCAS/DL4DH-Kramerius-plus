package cz.inqool.dl4dh.krameriusplus.rest.controller;

import cz.inqool.dl4dh.krameriusplus.api.user.request.UserRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.user.request.UserRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.user.request.UserRequestFacade;
import cz.inqool.dl4dh.krameriusplus.api.user.request.UserRequestListDto;
import cz.inqool.dl4dh.krameriusplus.api.user.request.message.MessageCreateDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "UserRequest", description = "Užívatelské žádosti")
@RestController
@RequestMapping(UserRequestApi.USER_REQUEST_PATH)
public class UserRequestApi {

    public static final String USER_REQUEST_PATH = "/api/user-requests";

    private final UserRequestFacade userRequestFacade;

    @Autowired
    public UserRequestApi(UserRequestFacade userRequestFacade) {
        this.userRequestFacade = userRequestFacade;
    }

    @PostMapping("/")
    public UserRequestDto createUserRequest(@RequestBody UserRequestCreateDto createDto) {
        return userRequestFacade.createUserRequest(createDto);
    }

    @GetMapping("/")
    public Page<UserRequestListDto> userRequest(@RequestBody Pageable pageable, @RequestParam boolean viewDeleted){
        return userRequestFacade.listPage(pageable, viewDeleted);
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
    public ResponseEntity<Void> createMessage(@PathVariable String requestId, @RequestBody MessageCreateDto messageCreateDto) {
        userRequestFacade.createMessage(requestId, messageCreateDto);

        return ResponseEntity.ok().build();
    }
}
