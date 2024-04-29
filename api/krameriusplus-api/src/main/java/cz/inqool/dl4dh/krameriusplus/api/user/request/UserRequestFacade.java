package cz.inqool.dl4dh.krameriusplus.api.user.request;

import cz.inqool.dl4dh.krameriusplus.api.user.request.document.DocumentState;
import cz.inqool.dl4dh.krameriusplus.api.user.request.message.MessageCreateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRequestFacade {

    UserRequestDto createUserRequest(UserRequestCreateDto createDto);

    Page<UserRequestListDto> listPage(Pageable pageable, boolean viewDeleted);

    UserRequestDto findById(String id);

    boolean checkFileAccessible(String requestId, String fileId);

    void createMessage(String requestId, MessageCreateDto messageCreateDto);

    boolean changeRequestState(String requestId, UserRequestState state, boolean overrideTransition);

    boolean changeDocumentState(String requestId, String documentId, DocumentState state, boolean forceTransition);
}
