package cz.inqool.dl4dh.krameriusplus.core.user.request.service;

import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.api.user.UserRole;
import cz.inqool.dl4dh.krameriusplus.api.user.request.UserRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.user.request.UserRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.user.request.UserRequestFacade;
import cz.inqool.dl4dh.krameriusplus.api.user.request.UserRequestListDto;
import cz.inqool.dl4dh.krameriusplus.api.user.request.UserRequestState;
import cz.inqool.dl4dh.krameriusplus.api.user.request.document.DocumentState;
import cz.inqool.dl4dh.krameriusplus.api.user.request.message.MessageCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.user.User;
import cz.inqool.dl4dh.krameriusplus.core.user.UserProvider;
import cz.inqool.dl4dh.krameriusplus.core.user.request.entity.UserRequest;
import cz.inqool.dl4dh.krameriusplus.core.user.request.entity.UserRequestFile;
import cz.inqool.dl4dh.krameriusplus.core.user.request.entity.UserRequestMessage;
import cz.inqool.dl4dh.krameriusplus.core.user.request.entity.UserRequestPart;
import cz.inqool.dl4dh.krameriusplus.core.user.request.mapper.UserRequestMapper;
import cz.inqool.dl4dh.krameriusplus.core.user.request.store.UserRequestFileStore;
import cz.inqool.dl4dh.krameriusplus.core.user.request.store.UserRequestMessageStore;
import cz.inqool.dl4dh.krameriusplus.core.user.request.store.UserRequestPartStore;
import cz.inqool.dl4dh.krameriusplus.core.user.request.store.UserRequestStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserRequestService implements UserRequestFacade {

    private final UserRequestMapper userRequestMapper;

    private final UserProvider userProvider;

    private final UserRequestStore userRequestStore;

    private final UserRequestPartStore userRequestPartStore;

    private final UserRequestFileStore userRequestFileStore;

    private final UserRequestMessageStore userRequestMessageStore;

    @Autowired
    public UserRequestService(UserRequestMapper userRequestMapper,
                              UserProvider userProvider,
                              UserRequestStore userRequestStore, UserRequestPartStore userRequestPartStore, UserRequestFileStore userRequestFileStore, UserRequestMessageStore userRequestMessageStore) {
        this.userRequestMapper = userRequestMapper;
        this.userProvider = userProvider;
        this.userRequestStore = userRequestStore;
        this.userRequestPartStore = userRequestPartStore;
        this.userRequestFileStore = userRequestFileStore;
        this.userRequestMessageStore = userRequestMessageStore;
    }

    @Override
    public UserRequestDto createUserRequest(UserRequestCreateDto createDto) {
        UserRequest userRequest = new UserRequest();
        userRequest.setUser(userProvider.getCurrentUser());
        userRequest.setType(createDto.getType());

        userRequest = userRequestStore.save(userRequest);

        for (String id : createDto.getPublicationIds()) {
            UserRequestPart userRequestPart = new UserRequestPart();
            userRequestPart.setUserRequest(userRequest);
            userRequestPart.setPublicationId(id);
            userRequestPart.setNote(createDto.getNote());
            userRequestPartStore.save(userRequestPart);
        }

        return userRequestMapper.toDto(userRequest);
    }

    @Override
    public Page<UserRequestListDto> listPage(Pageable pageable, boolean viewDeleted) {
        User currentUser = userProvider.getCurrentUser();

        Page<UserRequest> userRequests;
        if (currentUser.getRole().equals(UserRole.ADMIN)) {
            userRequests = userRequestStore.findAll(pageable, viewDeleted);
        } else {
            userRequests = userRequestStore.findAllForUser(pageable, currentUser.getId(), viewDeleted);
        }

        return userRequests.map(userRequestMapper::toListDto);
    }

    @Override
    public UserRequestDto findById(String id) {
        return userRequestMapper.toDto(findUserRequest(id));
    }


    @Override
    public boolean checkFileAccessible(String requestId, String fileId) {
        User currentUser = userProvider.getCurrentUser();

        UserRequestFile userRequestFile = userRequestFileStore.findById(fileId)
                .orElseThrow(() -> new MissingObjectException(UserRequestFile.class, fileId));

        return userRequestFile.getMessage().getUserRequest()
                .getUser()
                .equals(currentUser);
    }

    @Override
    public void createMessage(String requestId, MessageCreateDto messageCreateDto) {
        UserRequest userRequest = findUserRequest(requestId);
        User currentUser = userProvider.getCurrentUser();

        UserRequestMessage userRequestMessage = new UserRequestMessage();
        userRequestMessage.setUserRequest(userRequest);
        userRequestMessage.setAuthor(currentUser);
        userRequestMessage.setMessage(messageCreateDto.getMessage());

        userRequestMessageStore.save(userRequestMessage);
    }

    @Override
    public boolean changeRequestState(String requestId, UserRequestState state, boolean forceTransition) {
        UserRequest userRequest = findUserRequest(requestId);

        if (!forceTransition) {
            Set<UserRequestState> nextPossibleState = userRequest.getState().getValidTransitions();
            if (!nextPossibleState.contains(state)) {
                return false;
            }
        }

        userRequest.setState(state);
        userRequestStore.save(userRequest);
        return true;
    }

    @Override
    public boolean changeDocumentState(String requestId, String partId, DocumentState state, boolean forceTransition) {
        UserRequestPart part = userRequestPartStore.findById(partId)
                .orElseThrow(() -> new MissingObjectException(UserRequestPart.class, partId));

        if (!forceTransition) {
            Set<DocumentState> transitions = part.getState().getTransitions();
            if (!transitions.contains(state)) {
                return false;
            }
        }

        part.setState(state);
        userRequestPartStore.save(part);
        return true;
    }

    private UserRequest findUserRequest(String id) {
        return userRequestStore.findById(id)
                .orElseThrow(() -> new MissingObjectException(UserRequest.class, id));
    }
}
