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
import cz.inqool.dl4dh.krameriusplus.core.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.file.FileService;
import cz.inqool.dl4dh.krameriusplus.core.user.User;
import cz.inqool.dl4dh.krameriusplus.core.user.UserProvider;
import cz.inqool.dl4dh.krameriusplus.core.user.request.entity.UserRequest;
import cz.inqool.dl4dh.krameriusplus.core.user.request.entity.UserRequestMessage;
import cz.inqool.dl4dh.krameriusplus.core.user.request.entity.UserRequestPart;
import cz.inqool.dl4dh.krameriusplus.core.user.request.mapper.UserRequestMapper;
import cz.inqool.dl4dh.krameriusplus.core.user.request.store.UserRequestMessageStore;
import cz.inqool.dl4dh.krameriusplus.core.user.request.store.UserRequestPartStore;
import cz.inqool.dl4dh.krameriusplus.core.user.request.store.UserRequestStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserRequestService implements UserRequestFacade {

    private final UserRequestMapper userRequestMapper;

    private final UserProvider userProvider;

    private final UserRequestStore userRequestStore;

    private final UserRequestPartStore userRequestPartStore;

    private final UserRequestMessageStore userRequestMessageStore;

    private final FileService fileService;

    @Autowired
    public UserRequestService(UserRequestMapper userRequestMapper,
                              UserProvider userProvider,
                              UserRequestStore userRequestStore,
                              UserRequestPartStore userRequestPartStore,
                              UserRequestMessageStore userRequestMessageStore,
                              FileService fileService) {
        this.userRequestMapper = userRequestMapper;
        this.userProvider = userProvider;
        this.userRequestStore = userRequestStore;
        this.userRequestPartStore = userRequestPartStore;
        this.userRequestMessageStore = userRequestMessageStore;
        this.fileService = fileService;
    }

    @Override
    public UserRequestDto createUserRequest(UserRequestCreateDto createDto, List<MultipartFile> multipartFiles) {
        UserRequest userRequest = new UserRequest();
        User currentUser = userProvider.getCurrentUser();
        userRequest.setUser(currentUser);
        userRequest.setType(createDto.getType());

        userRequest = userRequestStore.saveAndFlush(userRequest);

        userRequest.setParts(createRequestParts(createDto, userRequest));
        userRequest.setMessages(Set.of(doCreateMessage(userRequest,
                new MessageCreateDto(createDto.getMessage(), multipartFiles), currentUser)));

        return userRequestMapper.toDto(userRequest);
    }

    private Set<FileRef> createRequestFiles(List<MultipartFile> multipartFiles) {
        Set<FileRef> result = new HashSet<>();
        for (MultipartFile multipartFile : multipartFiles) {
            try {
                FileRef fileRef = fileService.create(multipartFile.getInputStream(),
                        multipartFile.getSize(),
                        multipartFile.getName(),
                        multipartFile.getContentType());
                result.add(fileRef);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        return result;

    }

    private Set<UserRequestPart> createRequestParts(UserRequestCreateDto createDto, UserRequest userRequest) {
        Set<UserRequestPart> parts = new HashSet<>();

        for (String id : createDto.getPublicationIds()) {
            UserRequestPart userRequestPart = new UserRequestPart();
            userRequestPart.setUserRequest(userRequest);
            userRequestPart.setPublicationId(id);
            userRequestPart.setNote(createDto.getMessage());
            parts.add(userRequestPartStore.save(userRequestPart));
        }

        return parts;
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
        UserRequest request = userRequestStore.findById(requestId)
                .orElseThrow(() -> new MissingObjectException(UserRequest.class, requestId));

        boolean hasPermission = hasPermissionsForRequest(request);

        boolean exists = request.getMessages().stream()
                .flatMap(message -> message.getFiles().stream())
                .anyMatch(file -> file.getId().equals(fileId));

        return hasPermission && exists;
    }

    @Override
    public void createMessage(String requestId, MessageCreateDto messageCreateDto) {
        UserRequest userRequest = findUserRequest(requestId);
        doCreateMessage(userRequest, messageCreateDto, userProvider.getCurrentUser());
    }

    private UserRequestMessage doCreateMessage(UserRequest request, MessageCreateDto createDto, User currentUser) {
        UserRequestMessage userRequestMessage = new UserRequestMessage();
        userRequestMessage.setUserRequest(request);
        userRequestMessage.setAuthor(currentUser);
        userRequestMessage.setMessage(createDto.getMessage());
        userRequestMessage.setFiles(createRequestFiles(createDto.getFiles()));

        return userRequestMessageStore.save(userRequestMessage);
    }

    @Override
    public boolean changeRequestState(String requestId, UserRequestState state, boolean forceTransition) {
        if (!userProvider.getCurrentUser().getRole().equals(UserRole.ADMIN)) {
            return false;
        }

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
    public boolean changeDocumentState(String requestId, List<String> publicationIds, DocumentState state, boolean forceTransition) {
        if (!userProvider.getCurrentUser().getRole().equals(UserRole.ADMIN)) {
            return false;
        }

        List<UserRequestPart> requestParts = userRequestPartStore.findAllByPublicationIds(publicationIds);

        if (!forceTransition) {
            if (requestParts.stream()
                    .anyMatch(part -> !part.getState().getTransitions().contains(state))) {
                return false;
            }
        }

        requestParts.forEach(part -> part.setState(state));
        userRequestPartStore.saveAll(requestParts);
        return true;
    }

    private UserRequest findUserRequest(String id) {
        UserRequest userRequest = userRequestStore.findById(id)
                .orElseThrow(() -> new MissingObjectException(UserRequest.class, id));

        boolean hasPermissionToView = hasPermissionsForRequest(userRequest);

        // TODO: exception
        if (!hasPermissionToView) {
            throw new IllegalArgumentException("user has no permission to view, todo: add better exception");
        }

        return userRequest;
    }

    private boolean hasPermissionsForRequest(UserRequest userRequest) {
        User currentUser = userProvider.getCurrentUser();
        return userRequest.getUser().equals(currentUser) || currentUser.getRole().equals(UserRole.ADMIN);
    }
}
