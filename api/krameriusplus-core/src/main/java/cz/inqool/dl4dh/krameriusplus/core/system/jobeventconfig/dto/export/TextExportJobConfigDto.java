package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.export;

import cz.inqool.dl4dh.krameriusplus.core.system.bulkexport.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextExportJobConfigDto extends ExportJobConfigDto {

    @Override
    public KrameriusJob getKrameriusJob() {
        return KrameriusJob.EXPORT_TEXT;
    }

    @Override
    public ExportFormat getFormat() {
        return ExportFormat.TEXT;
    }
}
