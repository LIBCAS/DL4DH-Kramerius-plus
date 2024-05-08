package cz.inqool.dl4dh.krameriusplus.api.user.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Validated
public class UserRequestCreateDto {

    @NotNull
    private UserRequestType type;

    @Size(min = 1)
    private List<String> publicationIds;

    @NotNull
    private String message;
}
