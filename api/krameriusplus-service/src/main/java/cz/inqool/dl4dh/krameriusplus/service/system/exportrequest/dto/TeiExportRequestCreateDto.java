package cz.inqool.dl4dh.krameriusplus.service.system.exportrequest.dto;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.export.TeiExportJobConfigDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TeiExportRequestCreateDto extends ExportRequestCreateDto {

    @NotNull
    private TeiExportJobConfigDto config;
}
