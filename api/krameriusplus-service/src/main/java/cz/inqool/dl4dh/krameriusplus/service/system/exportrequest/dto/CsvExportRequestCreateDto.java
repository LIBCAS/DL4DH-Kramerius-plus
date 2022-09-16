package cz.inqool.dl4dh.krameriusplus.service.system.exportrequest.dto;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.export.CsvExportJobConfigDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CsvExportRequestCreateDto extends ExportRequestCreateDto {

    @NotNull
    private CsvExportJobConfigDto config;
}
