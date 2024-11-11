package cz.inqool.dl4dh.krameriusplus.core.user.request.service;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.domain.FileRefDto;
import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.api.request.ListFilterDto;
import cz.inqool.dl4dh.krameriusplus.api.request.UserRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.request.UserRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.request.UserRequestFacade;
import cz.inqool.dl4dh.krameriusplus.api.request.UserRequestListDto;
import cz.inqool.dl4dh.krameriusplus.api.request.UserRequestState;
import cz.inqool.dl4dh.krameriusplus.api.request.document.DocumentState;
import cz.inqool.dl4dh.krameriusplus.api.request.message.MessageCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.user.UserRole;
import cz.inqool.dl4dh.krameriusplus.core.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.file.FileRefMapper;
import cz.inqool.dl4dh.krameriusplus.core.file.FileService;
import cz.inqool.dl4dh.krameriusplus.core.user.User;
import cz.inqool.dl4dh.krameriusplus.core.user.UserProvider;
import cz.inqool.dl4dh.krameriusplus.core.user.request.entity.UserRequest;
import cz.inqool.dl4dh.krameriusplus.core.user.request.entity.UserRequestMessage;
import cz.inqool.dl4dh.krameriusplus.core.user.request.entity.UserRequestPart;
import cz.inqool.dl4dh.krameriusplus.core.user.request.entity.UserRequestStateAudit;
import cz.inqool.dl4dh.krameriusplus.core.user.request.mapper.UserRequestMapper;
import cz.inqool.dl4dh.krameriusplus.core.user.request.store.UserRequestMessageStore;
import cz.inqool.dl4dh.krameriusplus.core.user.request.store.UserRequestPartStore;
import cz.inqool.dl4dh.krameriusplus.core.user.request.store.UserRequestStateAuditStore;
import cz.inqool.dl4dh.krameriusplus.core.user.request.store.UserRequestStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserRequestService implements UserRequestFacade {

    private final UserRequestMapper userRequestMapper;

    private final UserProvider userProvider;

    private final UserRequestStore userRequestStore;

    private final UserRequestPartStore userRequestPartStore;

    private final UserRequestMessageStore userRequestMessageStore;

    private final UserRequestStateAuditStore userRequestStateAuditStore;

    private final FileService fileService;

    private final FileRefMapper fileRefMapper;

    @Autowired
    public UserRequestService(UserRequestMapper userRequestMapper,
                              UserProvider userProvider,
                              UserRequestStore userRequestStore,
                              UserRequestPartStore userRequestPartStore,
                              UserRequestMessageStore userRequestMessageStore,
                              UserRequestStateAuditStore userRequestStateAuditStore,
                              FileService fileService,
                              FileRefMapper fileRefMapper) {
        this.userRequestMapper = userRequestMapper;
        this.userProvider = userProvider;
        this.userRequestStore = userRequestStore;
        this.userRequestPartStore = userRequestPartStore;
        this.userRequestMessageStore = userRequestMessageStore;
        this.userRequestStateAuditStore = userRequestStateAuditStore;
        this.fileService = fileService;
        this.fileRefMapper = fileRefMapper;
    }

    @Override
    public UserRequestDto createUserRequest(UserRequestCreateDto createDto, List<MultipartFile> multipartFiles) {
        UserRequest userRequest = new UserRequest();
        User currentUser = userProvider.getCurrentUser();
        userRequest.setUser(currentUser);
        userRequest.setType(createDto.getType());

        userRequest = userRequestStore.saveAndFlush(userRequest);

        userRequest.setParts(createRequestParts(createDto, userRequest));
        userRequest.setMessages(List.of(doCreateMessage(userRequest,
                new MessageCreateDto(createDto.getMessage()), currentUser,
                multipartFiles)));

        return userRequestMapper.toDto(userRequest);
    }

    private Set<FileRef> createRequestFiles(List<MultipartFile> multipartFiles) {
        Set<FileRef> result = new HashSet<>();
        for (MultipartFile multipartFile : multipartFiles) {
            try {
                FileRef fileRef = fileService.create(multipartFile.getInputStream(),
                        multipartFile.getSize(),
                        multipartFile.getOriginalFilename(),
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
    public Result<UserRequestListDto> listPage(Pageable pageable, boolean viewDeleted, ListFilterDto filters) {
        User currentUser = userProvider.getCurrentUser();

        Page<UserRequest> userRequests;
        if (currentUser.getRole().equals(UserRole.ADMIN)) {
            userRequests = userRequestStore.findAll(pageable, filters, viewDeleted);
        } else {
            userRequests = userRequestStore.findAllForUser(pageable, filters, currentUser.getId(), viewDeleted);
        }

        return new Result<>(pageable.getPageNumber(), pageable.getPageSize(),
                userRequests.getTotalElements(),
                userRequests.getContent()
                .stream()
                .map(userRequestMapper::toListDto).collect(Collectors.toList()));
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
    public FileRefDto downloadFile(String requestId, String fileId) {
        UserRequest request = userRequestStore.findById(requestId)
                .orElseThrow(() -> new MissingObjectException(UserRequest.class, requestId));

        boolean hasPermission = hasPermissionsForRequest(request);

        Optional<FileRef> file = request.getMessages().stream()
                .flatMap(message -> message.getFiles().stream())
                .filter(f -> f.getId().equals(fileId)).findFirst();

        if (file.isPresent() && hasPermission) {
            return fileRefMapper.toDto(file.get());
        }
        return null;
    }

    @Override
    public void createMessage(String requestId, MessageCreateDto messageCreateDto, List<MultipartFile> files) {
        UserRequest userRequest = findUserRequest(requestId);
        doCreateMessage(userRequest, messageCreateDto, userProvider.getCurrentUser(), files);
    }

    private UserRequestMessage doCreateMessage(UserRequest request, MessageCreateDto createDto,
                                               User currentUser, List<MultipartFile> files) {
        UserRequestMessage userRequestMessage = new UserRequestMessage();
        userRequestMessage.setUserRequest(request);
        userRequestMessage.setAuthor(currentUser);
        userRequestMessage.setMessage(createDto.getMessage());
        userRequestMessage.setFiles(createRequestFiles(files));

        return userRequestMessageStore.save(userRequestMessage);
    }

    @Override
    public boolean changeRequestState(String requestId, UserRequestState state, boolean forceTransition) {
        User loggedInUser = userProvider.getCurrentUser();

        assert loggedInUser != null : "User must be logged in when changing request state.";

        if (!loggedInUser.getRole().equals(UserRole.ADMIN)) {
            return false;
        }

        UserRequest userRequest = findUserRequest(requestId);

        if (!forceTransition) {
            Set<UserRequestState> nextPossibleState = userRequest.getState().getValidTransitions();
            if (!nextPossibleState.contains(state)) {
                return false;
            }
        }

        createChangeAudit(userRequest, state, loggedInUser);
        userRequest.setState(state);
        userRequestStore.save(userRequest);
        return true;
    }

    private void createChangeAudit(UserRequest userRequest, UserRequestState stateAfter, User author) {
        UserRequestStateAudit userRequestStateAudit = new UserRequestStateAudit();
        userRequestStateAudit.setAfter(stateAfter);

        assert userRequest.getState() != null : "User request state cannot be null when creating audit, request id: " + userRequest.getId();

        userRequestStateAudit.setBefore(userRequest.getState());
        userRequestStateAudit.setUser(author);
        userRequestStateAudit.setUserRequest(userRequest);
        userRequestStateAuditStore.save(userRequestStateAudit);
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

        if (!hasPermissionToView) {
            throw new AccessDeniedException("Logged in user does not have permission to view: " + id);
        }

        return userRequest;
    }

    private boolean hasPermissionsForRequest(UserRequest userRequest) {
        User currentUser = userProvider.getCurrentUser();
        return userRequest.getUser().equals(currentUser) || currentUser.getRole().equals(UserRole.ADMIN);
    }
}
