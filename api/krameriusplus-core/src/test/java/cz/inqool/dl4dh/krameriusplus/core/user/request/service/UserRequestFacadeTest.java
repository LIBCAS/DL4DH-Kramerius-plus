package cz.inqool.dl4dh.krameriusplus.core.user.request.service;

import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.api.user.UserRole;
import cz.inqool.dl4dh.krameriusplus.api.user.request.UserRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.user.request.UserRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.user.request.UserRequestFacade;
import cz.inqool.dl4dh.krameriusplus.api.user.request.UserRequestListDto;
import cz.inqool.dl4dh.krameriusplus.api.user.request.UserRequestState;
import cz.inqool.dl4dh.krameriusplus.api.user.request.UserRequestType;
import cz.inqool.dl4dh.krameriusplus.api.user.request.message.MessageCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.user.request.message.MessageDto;
import cz.inqool.dl4dh.krameriusplus.core.CoreBaseTest;
import cz.inqool.dl4dh.krameriusplus.core.user.User;
import cz.inqool.dl4dh.krameriusplus.core.user.UserProvider;
import cz.inqool.dl4dh.krameriusplus.core.user.UserStore;
import cz.inqool.dl4dh.krameriusplus.core.user.request.entity.UserRequest;
import cz.inqool.dl4dh.krameriusplus.core.user.request.store.UserRequestStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.user.UserProvider.MOCK_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class UserRequestFacadeTest extends CoreBaseTest {

    @Autowired
    private UserRequestFacade userRequestFacade;

    @Autowired
    private UserRequestStore store;

    @Autowired
    private UserStore userStore;

    @MockBean
    private UserProvider userProvider;

    private User user;

    private User admin;

    private boolean setupDone = false;

    @BeforeEach
    public void initUsers() {
        if (setupDone) {
            return;
        }

        User user = new User();
        user.setUsername(MOCK_USERNAME);
        user.setRole(UserRole.USER);
        this.user = userStore.save(user);

        User admin = new User();
        admin.setUsername("admin");
        admin.setRole(UserRole.ADMIN);
        this.admin = userStore.save(admin);

        setupDone = true;
    }

    @AfterEach
    public void tearDown() {
        userStore.deleteAll();
        store.deleteAll();
    }

    @Test
    void createUserRequest_validArguments_createsUserRequest() {
        UserRequestCreateDto userRequestCreateDto = new UserRequestCreateDto();
        userRequestCreateDto.setType(UserRequestType.ENRICHMENT);
        userRequestCreateDto.setPublicationIds(List.of("publication-1"));

        when(userProvider.getCurrentUser()).thenReturn(user);
        UserRequestDto userRequest = userRequestFacade.createUserRequest(userRequestCreateDto, new ArrayList<>());

        assertThat(userRequest.getType()).isEqualTo(UserRequestType.ENRICHMENT);
        assertThat(userRequest.getUserRequestParts().size()).isEqualTo(1);
        assertThat(userRequest.getIdentification()).isNotBlank();
        assertThat(userRequest.getUsername()).isEqualTo(MOCK_USERNAME);
    }

    @Test
    void listPage_userLoggedIn_listsUsersRequestsOnly() {
        UserRequestCreateDto userRequestCreateDto = new UserRequestCreateDto();
        userRequestCreateDto.setType(UserRequestType.ENRICHMENT);
        userRequestCreateDto.setPublicationIds(List.of("publication-1"));

        when(userProvider.getCurrentUser()).thenReturn(user);
        userRequestFacade.createUserRequest(userRequestCreateDto, new ArrayList<>());
        userRequestFacade.createUserRequest(userRequestCreateDto, new ArrayList<>());
        userRequestFacade.createUserRequest(userRequestCreateDto, new ArrayList<>());

        UserRequest userRequest = new UserRequest();
        userRequest.setState(UserRequestState.CREATED);
        User user = new User();
        user.setUsername("admin");
        user.setRole(UserRole.ADMIN);
        userRequest.setUser(user);

        store.save(userRequest);

        when(userProvider.getCurrentUser()).thenReturn(user);
        Page<UserRequestListDto> userRequestListDtos = userRequestFacade.listPage(Pageable.ofSize(10), false);

        assertThat(userRequestListDtos.getTotalElements()).isEqualTo(3);
        assertThat(userRequestListDtos.getContent().stream()
                .allMatch(request -> request.getUsername().equals(MOCK_USERNAME))).isTrue();
    }

    @Test
    void listPage_adminLoggedIn_listsAllRequests() {
        UserRequestCreateDto userRequestCreateDto = new UserRequestCreateDto();
        userRequestCreateDto.setType(UserRequestType.ENRICHMENT);
        userRequestCreateDto.setPublicationIds(List.of("publication-1"));

        when(userProvider.getCurrentUser()).thenReturn(user);
        userRequestFacade.createUserRequest(userRequestCreateDto, new ArrayList<>());
        userRequestFacade.createUserRequest(userRequestCreateDto, new ArrayList<>());
        userRequestFacade.createUserRequest(userRequestCreateDto, new ArrayList<>());

        UserRequest userRequest = new UserRequest();
        userRequest.setState(UserRequestState.CREATED);
        User user = new User();
        user.setUsername("admin");
        user.setRole(UserRole.ADMIN);
        userRequest.setUser(user);

        store.save(userRequest);

        when(userProvider.getCurrentUser()).thenReturn(admin);
        Page<UserRequestListDto> userRequestListDtos = userRequestFacade.listPage(Pageable.ofSize(10), false);

        assertThat(userRequestListDtos.getTotalElements()).isEqualTo(4);
    }

    @Test
    void findById_requestsExists_returnsRequest() {
        UserRequestCreateDto userRequestCreateDto = new UserRequestCreateDto();
        userRequestCreateDto.setType(UserRequestType.ENRICHMENT);
        userRequestCreateDto.setPublicationIds(List.of("publication-1"));

        when(userProvider.getCurrentUser()).thenReturn(user);
        UserRequestDto userRequest = userRequestFacade.createUserRequest(userRequestCreateDto, new ArrayList<>());

        UserRequestDto retrieved = userRequestFacade.findById(userRequest.getId());
        assertThat(retrieved).isNotNull();
    }

    @Test
    void findById_requestMissing_missingObjectThrown() {
        assertThatThrownBy(() -> userRequestFacade.findById("does not exist"))
                .isInstanceOf(MissingObjectException.class);
    }

    @Test
    void findById_userHasNoPermission_unauthorized() {
        UserRequestCreateDto userRequestCreateDto = new UserRequestCreateDto();
        userRequestCreateDto.setType(UserRequestType.ENRICHMENT);
        userRequestCreateDto.setPublicationIds(List.of("publication-1"));

        when(userProvider.getCurrentUser()).thenReturn(admin);
        UserRequestDto userRequest = userRequestFacade.createUserRequest(userRequestCreateDto, new ArrayList<>());

        when(userProvider.getCurrentUser()).thenReturn(user);
        assertThatThrownBy(() -> userRequestFacade.findById(userRequest.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void checkFileAccessible_fileExistsAndIsOwnedByUser_returnsTrue() {
        UserRequestCreateDto userRequestCreateDto = new UserRequestCreateDto();
        userRequestCreateDto.setType(UserRequestType.ENRICHMENT);
        userRequestCreateDto.setPublicationIds(List.of("publication-1"));
        MockMultipartFile file = new MockMultipartFile("test", "test.txt",
                "text/plain", "content".getBytes(StandardCharsets.UTF_8));

        when(userProvider.getCurrentUser()).thenReturn(admin);
        UserRequestDto userRequest = userRequestFacade.createUserRequest(userRequestCreateDto, List.of(file));

        when(userProvider.getCurrentUser()).thenReturn(admin);
        assertThat(userRequest.getMessages().size()).isEqualTo(1);
        assertThat(userRequestFacade.checkFileAccessible(userRequest.getId(),
                userRequest.getMessages().get(0).getFiles().get(0).getId()))
                .isTrue();
    }

    @Test
    void checkFileAccessible_fileMissing_throwsMissingObject() {
        UserRequestCreateDto userRequestCreateDto = new UserRequestCreateDto();
        userRequestCreateDto.setType(UserRequestType.ENRICHMENT);
        userRequestCreateDto.setPublicationIds(List.of("publication-1"));

        when(userProvider.getCurrentUser()).thenReturn(admin);
        UserRequestDto userRequest = userRequestFacade.createUserRequest(userRequestCreateDto, new ArrayList<>());

        when(userProvider.getCurrentUser()).thenReturn(admin);
        assertThat(userRequest.getMessages().size()).isEqualTo(0);
        assertThat(userRequestFacade.checkFileAccessible(userRequest.getId(), "test")).isFalse();
    }

    @Test
    void checkFileAccessible_userDoesNotHavePermissions_returnsFalse() {
        UserRequestCreateDto userRequestCreateDto = new UserRequestCreateDto();
        userRequestCreateDto.setType(UserRequestType.ENRICHMENT);
        userRequestCreateDto.setPublicationIds(List.of("publication-1"));
        MockMultipartFile file = new MockMultipartFile("test", "test.txt",
                "text/plain", "content".getBytes(StandardCharsets.UTF_8));

        when(userProvider.getCurrentUser()).thenReturn(admin);
        UserRequestDto userRequest = userRequestFacade.createUserRequest(userRequestCreateDto, List.of(file));

        when(userProvider.getCurrentUser()).thenReturn(user);
        assertThat(userRequest.getMessages().size()).isEqualTo(1);
        assertThat(userRequestFacade.checkFileAccessible(userRequest.getId(), userRequest
                .getMessages().get(0).getFiles().get(0).getId()))
                .isFalse();
    }

    @Test
    void createMessage_requestExists_messageIsCreated() {
        UserRequestCreateDto userRequestCreateDto = new UserRequestCreateDto();
        userRequestCreateDto.setType(UserRequestType.ENRICHMENT);
        userRequestCreateDto.setPublicationIds(List.of("publication-1"));
        MockMultipartFile file = new MockMultipartFile("test", "test.txt",
                "text/plain", "content".getBytes(StandardCharsets.UTF_8));

        when(userProvider.getCurrentUser()).thenReturn(admin);
        UserRequestDto userRequest = userRequestFacade.createUserRequest(userRequestCreateDto, List.of(file));

        MessageCreateDto messageCreateDto = new MessageCreateDto();
        messageCreateDto.setFiles(List.of(file));
        messageCreateDto.setMessage("test message");

        userRequestFacade.createMessage(userRequest.getId(), messageCreateDto);

        UserRequestDto retrieved = userRequestFacade.findById(userRequest.getId());
        assertThat(retrieved.getMessages().size()).isEqualTo(1);
        MessageDto message = retrieved.getMessages().get(0);
        assertThat(message.getFiles().size()).isEqualTo(1);
        assertThat(message.getContent()).isEqualTo("test message");
    }

    @Test
    void createMessage_requestDoesNotExist_missingObjectThrown() {
        UserRequestCreateDto userRequestCreateDto = new UserRequestCreateDto();
        userRequestCreateDto.setType(UserRequestType.ENRICHMENT);
        userRequestCreateDto.setPublicationIds(List.of("publication-1"));
        MockMultipartFile file = new MockMultipartFile("test", "test.txt",
                "text/plain", "content".getBytes(StandardCharsets.UTF_8));

        when(userProvider.getCurrentUser()).thenReturn(admin);
        UserRequestDto userRequest = userRequestFacade.createUserRequest(userRequestCreateDto, List.of(file));

        MessageCreateDto messageCreateDto = new MessageCreateDto();
        messageCreateDto.setFiles(List.of(file));
        messageCreateDto.setMessage("test message");

        assertThatThrownBy(() -> userRequestFacade.createMessage(userRequest.getId(), messageCreateDto))
                .isInstanceOf(MissingObjectException.class);

    }

    @Test
    void changeRequestState_transitionIsValid_stateChanges() {
        UserRequestCreateDto userRequestCreateDto = new UserRequestCreateDto();
        userRequestCreateDto.setType(UserRequestType.ENRICHMENT);
        userRequestCreateDto.setPublicationIds(List.of("publication-1"));
        MockMultipartFile file = new MockMultipartFile("test", "test.txt",
                "text/plain", "content".getBytes(StandardCharsets.UTF_8));

        when(userProvider.getCurrentUser()).thenReturn(admin);
        UserRequestDto userRequest = userRequestFacade.createUserRequest(userRequestCreateDto, List.of(file));

        boolean changed = userRequestFacade.changeRequestState(userRequest.getId(), UserRequestState.WAITING_FOR_USER, false);

        assertThat(changed).isTrue();

        UserRequestDto retrieved = userRequestFacade.findById(userRequest.getId());
        assertThat(retrieved.getState()).isEqualTo(UserRequestState.WAITING_FOR_USER);
    }

    @Test
    void changeRequestState_transitionIsForced_stateChanges() {
        UserRequestCreateDto userRequestCreateDto = new UserRequestCreateDto();
        userRequestCreateDto.setType(UserRequestType.ENRICHMENT);
        userRequestCreateDto.setPublicationIds(List.of("publication-1"));
        MockMultipartFile file = new MockMultipartFile("test", "test.txt",
                "text/plain", "content".getBytes(StandardCharsets.UTF_8));

        when(userProvider.getCurrentUser()).thenReturn(admin);
        UserRequestDto userRequest = userRequestFacade.createUserRequest(userRequestCreateDto, List.of(file));

        boolean changed = userRequestFacade.changeRequestState(userRequest.getId(), UserRequestState.REJECTED, true);

        assertThat(changed).isTrue();

        UserRequestDto retrieved = userRequestFacade.findById(userRequest.getId());
        assertThat(retrieved.getState()).isEqualTo(UserRequestState.REJECTED);
    }

    @Test
    void changeRequestState_transitionIsInvalid_exceptionThrown() {
        UserRequestCreateDto userRequestCreateDto = new UserRequestCreateDto();
        userRequestCreateDto.setType(UserRequestType.ENRICHMENT);
        userRequestCreateDto.setPublicationIds(List.of("publication-1"));
        MockMultipartFile file = new MockMultipartFile("test", "test.txt",
                "text/plain", "content".getBytes(StandardCharsets.UTF_8));

        when(userProvider.getCurrentUser()).thenReturn(admin);
        UserRequestDto userRequest = userRequestFacade.createUserRequest(userRequestCreateDto, List.of(file));

        boolean changed = userRequestFacade
                .changeRequestState(userRequest.getId(), UserRequestState.WAITING_FOR_USER, false);

        assertThat(changed).isFalse();
    }
}