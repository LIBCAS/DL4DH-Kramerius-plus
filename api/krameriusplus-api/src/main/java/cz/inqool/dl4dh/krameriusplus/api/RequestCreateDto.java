package cz.inqool.dl4dh.krameriusplus.api;

import cz.inqool.dl4dh.krameriusplus.api.domain.DatedObjectCreateDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class RequestCreateDto extends DatedObjectCreateDto {

    /**
     * Optional name for the request
     */
    private String name;

    /**
     * List of root publication UUIDs to process in the request
     */
    @NotNull
    @Size(min = 1, message = "PublicationIds cannot be empty.")
    private List<@NotBlank(message = "PublicationIds cannot be blank.") String> publicationIds = new ArrayList<>();
}
