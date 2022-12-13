package cz.inqool.dl4dh.krameriusplus.api.batch.config;

import cz.inqool.dl4dh.krameriusplus.api.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import lombok.Getter;

@Getter
public class ExportTextJobConfigDto extends ExportJobConfigDto {

    private final KrameriusJobType jobType = KrameriusJobType.EXPORT_TEXT;

    @Override
    public ExportFormat getExportFormat() {
        return ExportFormat.TEXT;
    }
}
