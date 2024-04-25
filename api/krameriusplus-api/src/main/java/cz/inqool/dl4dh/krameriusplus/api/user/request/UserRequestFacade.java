package cz.inqool.dl4dh.krameriusplus.api.user.request;

import cz.inqool.dl4dh.krameriusplus.api.user.request.message.MessageCreateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRequestFacade {

    UserRequestDto createUserRequest(UserRequestCreateDto createDto);

    Page<UserRequestListDto> listPage(Pageable pageable, boolean viewDeleted);

    UserRequestDto findById(String id);

    boolean checkFileAccessible(String requestId, String fileId);

    void createMessage(String requestId, MessageCreateDto messageCreateDto);
}
