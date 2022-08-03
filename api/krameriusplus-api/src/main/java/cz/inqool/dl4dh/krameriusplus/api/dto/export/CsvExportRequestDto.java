package cz.inqool.dl4dh.krameriusplus.api.dto.export;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.export.CsvExportJobConfigDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CsvExportRequestDto extends ExportRequestBase {

    @NotNull
    private CsvExportJobConfigDto config;

    public CsvExportRequestDto(String name, @NotNull String publicationId, CsvExportJobConfigDto config) {
        super(name, publicationId);
        this.config = config;
    }
}
