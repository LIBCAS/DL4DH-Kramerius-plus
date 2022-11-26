package cz.inqool.dl4dh.krameriusplus.api.batch.config;

import cz.inqool.dl4dh.krameriusplus.api.ExportFormat;

public class ExportTeiJobConfigDto extends ExportJobConfigDto {

    @Override
    public ExportFormat getExportFormat() {
        return ExportFormat.TEI;
    }
}
