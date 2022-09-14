package cz.inqool.dl4dh.krameriusplus.api.dto.export;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.export.TextExportJobConfigDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.dto.ExportRequestCreateDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class TextExportRequestCreateDto extends ExportRequestCreateDto {

    @NotNull
    private TextExportJobConfigDto config;
}
