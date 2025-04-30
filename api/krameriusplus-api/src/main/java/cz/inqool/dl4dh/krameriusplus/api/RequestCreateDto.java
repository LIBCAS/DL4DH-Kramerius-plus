package cz.inqool.dl4dh.krameriusplus.api;

import cz.inqool.dl4dh.krameriusplus.api.domain.DatedObjectCreateDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "PublicationIds cannot be empty.")
    private List<String> publicationIds = new ArrayList<>();
}
