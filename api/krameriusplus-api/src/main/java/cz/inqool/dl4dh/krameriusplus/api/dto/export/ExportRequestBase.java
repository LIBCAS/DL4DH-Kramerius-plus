package cz.inqool.dl4dh.krameriusplus.api.dto.export;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public abstract class ExportRequestBase implements ExportRequestDto {

    protected String name;

    @NotNull
    protected String publicationId;
}
