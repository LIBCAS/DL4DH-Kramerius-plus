package cz.inqool.dl4dh.krameriusplus.api.request;

import cz.inqool.dl4dh.krameriusplus.api.request.message.MessageDto;
import cz.inqool.dl4dh.krameriusplus.api.request.part.UserRequestPartDto;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserRequestDto {

    private String id;

    private Instant created;

    private Instant updated;

    private UserRequestType type;

    private UserRequestState state;

    private String username;

    private String identification;

    private List<UserRequestPartDto> parts = new ArrayList<>();

    private List<MessageDto> messages = new ArrayList<>();
}
