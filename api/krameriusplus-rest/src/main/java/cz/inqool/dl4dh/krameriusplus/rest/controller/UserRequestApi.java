package cz.inqool.dl4dh.krameriusplus.rest.controller;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.request.ListFilterDto;
import cz.inqool.dl4dh.krameriusplus.api.request.Sort;
import cz.inqool.dl4dh.krameriusplus.api.request.UserRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.request.UserRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.request.UserRequestFacade;
import cz.inqool.dl4dh.krameriusplus.api.request.UserRequestListDto;
import cz.inqool.dl4dh.krameriusplus.api.request.UserRequestState;
import cz.inqool.dl4dh.krameriusplus.api.request.UserRequestType;
import cz.inqool.dl4dh.krameriusplus.api.request.message.MessageCreateDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.ArrayList;
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
    public ResponseEntity<UserRequestDto> createUserRequest(@Valid @ModelAttribute UserRequestCreateDto createDto,
                                                            @RequestParam("files") MultipartFile[] multipartFiles) {
        return new ResponseEntity<>(userRequestFacade
                .createUserRequest(createDto, Arrays.asList(multipartFiles)), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public Result<UserRequestListDto> userRequest(@RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                  @RequestParam(value = "year") Integer year,
                                                  @RequestParam(value = "identification") Integer identification,
                                                  @RequestParam(value = "state") UserRequestState state,
                                                  @RequestParam(value = "type") UserRequestType type,
                                                  @RequestParam(value = "username") String username,
                                                  @RequestParam(value = "sortOrder", defaultValue = "DESC") Sort.Order order,
                                                  @RequestParam(value = "sortField", defaultValue = "CREATED") Sort.Field field,
                                                  @RequestParam(value = "rootFilterOperation", defaultValue = "OR") ListFilterDto.RootFilterOperation operation,
                                                  @RequestParam(value = "viewDeleted", defaultValue = "false") boolean viewDeleted) {
        return userRequestFacade.listPage(
                Pageable.ofSize(pageSize).withPage(page),
                viewDeleted,
                ListFilterDto.builder()
                        .rootFilterOperation(operation)
                        .year(year)
                        .identification(identification)
                        .state(state)
                        .type(type)
                        .username(username)
                        .order(order)
                        .field(field)
                        .build());
    }

    @GetMapping("/{requestId}")
    public UserRequestDto findUserRequest(@PathVariable String requestId) {
        return userRequestFacade.findById(requestId);
    }

    @GetMapping("/{requestId}/file/{fileId}")
    public boolean checkFileAccessible(@PathVariable String requestId, @PathVariable String fileId) {
        return userRequestFacade.checkFileAccessible(requestId, fileId);
    }

    @PostMapping(value = "/{requestId}/message", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createMessage(@PathVariable String requestId,
                                              @Valid @ModelAttribute MessageCreateDto messageCreateDto,
                                              @RequestParam(value = "files", required = false) MultipartFile[] multipartFiles) {
        userRequestFacade.createMessage(requestId, messageCreateDto, multipartFiles != null ? Arrays.asList(multipartFiles) : new ArrayList<>());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
