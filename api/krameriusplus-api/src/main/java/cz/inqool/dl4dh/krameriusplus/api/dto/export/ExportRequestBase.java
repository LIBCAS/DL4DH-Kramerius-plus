package cz.inqool.dl4dh.krameriusplus.api.dto.export;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class ExportRequestBase implements ExportRequestDto {

    protected String name;

    @NotNull
    protected Set<String> publications;
}
