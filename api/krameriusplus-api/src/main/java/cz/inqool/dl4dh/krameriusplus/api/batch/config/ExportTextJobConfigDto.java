package cz.inqool.dl4dh.krameriusplus.api.batch.config;

import cz.inqool.dl4dh.krameriusplus.api.ExportFormat;

public class ExportTextJobConfigDto extends ExportJobConfigDto {

    @Override
    public ExportFormat getExportFormat() {
        return ExportFormat.TEXT;
    }
}
