package cz.inqool.dl4dh.krameriusplus.api.user.request;

import cz.inqool.dl4dh.krameriusplus.api.user.request.part.UserRequestPartDto;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class UserRequestDto {

    private Instant created;

    private Instant updated;

    private UserRequestType type;

    private UserRequestState state;

    private String identification;

    private List<UserRequestPartDto> userRequestParts;
}
