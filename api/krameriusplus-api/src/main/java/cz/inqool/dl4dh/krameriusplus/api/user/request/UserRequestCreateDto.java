package cz.inqool.dl4dh.krameriusplus.api.user.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRequestCreateDto {

    private UserRequestType type;

    private List<String> publicationIds;

    private String note;

    // TODO: subory
}
