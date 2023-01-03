package cz.inqool.dl4dh.krameriusplus.api.batch.config;

import cz.inqool.dl4dh.krameriusplus.api.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import lombok.Getter;

import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.EXPORT_JSON;

@Getter
public class ExportJsonJobConfigDto extends ExportJobConfigDto {

    private final KrameriusJobType jobType = EXPORT_JSON;

    @Override
    public ExportFormat getExportFormat() {
        return ExportFormat.JSON;
    }
}
