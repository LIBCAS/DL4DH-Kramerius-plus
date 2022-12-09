package cz.inqool.dl4dh.krameriusplus.corev2.job.config.export;

import cz.inqool.dl4dh.krameriusplus.api.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class ExportAltoJobConfig extends ExportJobConfig {

    @Override
    public KrameriusJobType getJobType() {
        return KrameriusJobType.EXPORT_ALTO;
    }

    @Override
    public ExportFormat getExportFormat() {
        return ExportFormat.ALTO;
    }
}
