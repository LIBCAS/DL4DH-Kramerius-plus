package cz.inqool.dl4dh.krameriusplus.api.dto.export;

import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.jobeventconfig.dto.export.CsvExportJobConfigDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class CsvExportRequestDto implements ExportRequestDto {

    @NotNull
    private String publicationId;

    @NotNull
    private CsvExportJobConfigDto config;
}
