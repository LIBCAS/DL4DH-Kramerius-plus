package cz.inqool.dl4dh.krameriusplus.api;

import cz.inqool.dl4dh.krameriusplus.api.domain.DatedObjectDto;
import cz.inqool.dl4dh.krameriusplus.api.user.UserDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class RequestDto extends DatedObjectDto {

    private String name;

    private List<String> publicationIds = new ArrayList<>();

    private UserDto owner;

    private RequestState state;
}
