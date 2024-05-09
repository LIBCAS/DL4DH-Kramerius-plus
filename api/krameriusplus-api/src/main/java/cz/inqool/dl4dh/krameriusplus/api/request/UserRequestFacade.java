package cz.inqool.dl4dh.krameriusplus.api.request;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.request.document.DocumentState;
import cz.inqool.dl4dh.krameriusplus.api.request.message.MessageCreateDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserRequestFacade {

    UserRequestDto createUserRequest(UserRequestCreateDto createDto, List<MultipartFile> files);

    Result<UserRequestListDto> listPage(Pageable pageable, boolean viewDeleted);

    UserRequestDto findById(String id);

    boolean checkFileAccessible(String requestId, String fileId);

    void createMessage(String requestId, MessageCreateDto messageCreateDto, List<MultipartFile> files);

    boolean changeRequestState(String requestId, UserRequestState state, boolean overrideTransition);

    boolean changeDocumentState(String requestId, List<String> documentId, DocumentState state, boolean forceTransition);
}
