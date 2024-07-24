package cz.inqool.dl4dh.krameriusplus.api.request.audit;

import cz.inqool.dl4dh.krameriusplus.api.request.UserRequestState;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class StateAuditDto {

    private Instant created;

    private String username;

    private UserRequestState before;

    private UserRequestState after;
}
