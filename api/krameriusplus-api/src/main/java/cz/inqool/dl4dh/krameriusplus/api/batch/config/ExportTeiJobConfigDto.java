package cz.inqool.dl4dh.krameriusplus.api.batch.config;

import cz.inqool.dl4dh.krameriusplus.api.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.api.export.params.TeiParamsDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportTeiJobConfigDto extends ExportJobConfigDto {

    private TeiParamsDto teiParams = new TeiParamsDto();

    @Override
    public ExportFormat getExportFormat() {
        return ExportFormat.TEI;
    }
}
