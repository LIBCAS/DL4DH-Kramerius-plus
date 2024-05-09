package cz.inqool.dl4dh.krameriusplus.api.request;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UserRequestListDto {

    private Instant created;

    private Instant updated;

    private String username;

    private UserRequestType type;

    private UserRequestState state;

    private String identification;
}
