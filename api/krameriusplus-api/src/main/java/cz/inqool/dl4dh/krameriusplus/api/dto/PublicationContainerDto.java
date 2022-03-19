package cz.inqool.dl4dh.krameriusplus.api.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
public class PublicationContainerDto {

    @NotNull
    private Set<String> publications;
}
