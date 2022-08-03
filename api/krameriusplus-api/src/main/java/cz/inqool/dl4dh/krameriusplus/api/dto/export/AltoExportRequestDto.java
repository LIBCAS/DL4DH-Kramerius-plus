package cz.inqool.dl4dh.krameriusplus.api.dto.export;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.export.AltoExportJobConfigDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AltoExportRequestDto extends ExportRequestBase {

    @NotNull
    private AltoExportJobConfigDto config;

    public AltoExportRequestDto(String name, @NotNull String publicationId, AltoExportJobConfigDto config) {
        super(name, publicationId);
        this.config = config;
    }
}
