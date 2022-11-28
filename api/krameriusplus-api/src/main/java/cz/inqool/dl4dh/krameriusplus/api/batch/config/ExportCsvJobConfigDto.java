package cz.inqool.dl4dh.krameriusplus.api.batch.config;

import cz.inqool.dl4dh.krameriusplus.api.ExportFormat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportCsvJobConfigDto extends ExportJobConfigDto {

    private String delimiter = ",";

    @Override
    public ExportFormat getExportFormat() {
        return ExportFormat.CSV;
    }
}
