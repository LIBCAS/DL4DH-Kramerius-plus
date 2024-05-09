package cz.inqool.dl4dh.krameriusplus.api.request.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Validated
public class ChangeDocumentStatesDto {

    @NotNull
    @Size(min = 1, message = "No document ids were provided for change of state")
    List<String> publicationIds = new ArrayList<>();

    @NotNull
    DocumentState state;

    boolean forceTransitions = false;
}
