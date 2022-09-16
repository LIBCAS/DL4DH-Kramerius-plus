package cz.inqool.dl4dh.krameriusplus.api.dto.export;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.export.JsonExportJobConfigDto;
import cz.inqool.dl4dh.krameriusplus.service.system.exportrequest.dto.ExportRequestCreateDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class JsonExportRequestCreateDto extends ExportRequestCreateDto {

    @NotNull
    private JsonExportJobConfigDto config;
}
