package cz.inqool.dl4dh.krameriusplus.api.batch.config;

import cz.inqool.dl4dh.krameriusplus.api.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import lombok.Getter;
import lombok.Setter;

import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.EXPORT_CSV;

@Getter
@Setter
public class ExportCsvJobConfigDto extends ExportJobConfigDto {

    private final KrameriusJobType jobType = EXPORT_CSV;

    private String delimiter = ",";

    @Override
    public ExportFormat getExportFormat() {
        return ExportFormat.CSV;
    }
}
