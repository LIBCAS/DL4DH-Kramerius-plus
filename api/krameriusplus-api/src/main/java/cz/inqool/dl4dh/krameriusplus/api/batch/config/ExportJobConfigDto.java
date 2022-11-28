package cz.inqool.dl4dh.krameriusplus.api.batch.config;

import cz.inqool.dl4dh.krameriusplus.api.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.api.export.params.ParamsDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ExportJobConfigDto extends JobConfigDto {

    private ParamsDto params = new ParamsDto();

    public abstract ExportFormat getExportFormat();
}
