package cz.inqool.dl4dh.krameriusplus.api.dto.export;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.export.AltoExportJobConfigDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AltoExportCreationRequestDto extends ExportCreationRequestBase {

    @NotNull
    private AltoExportJobConfigDto config;
}
