package cz.inqool.dl4dh.krameriusplus.corev2.job.config.export;

import cz.inqool.dl4dh.krameriusplus.api.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class ExportTextJobConfig extends ExportJobConfig {

    @Override
    public KrameriusJobType getJobType() {
        return KrameriusJobType.EXPORT_TEXT;
    }

    @Override
    public ExportFormat getExportFormat() {
        return ExportFormat.TEXT;
    }
}
