package cz.inqool.dl4dh.krameriusplus.api.dto.export;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class ExportRequestCreateDto {

    protected String name;

    @NotNull
    @NotEmpty
    protected Set<String> publications = new HashSet<>();
}
