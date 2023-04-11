package cz.inqool.dl4dh.krameriusplus.core.job.config.export;

import cz.inqool.dl4dh.krameriusplus.api.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@DiscriminatorValue("ExportTeiJobConfig")
public class ExportTeiJobConfigView extends ExportJobConfigView {

    @Override
    public KrameriusJobType getJobType() {
        return KrameriusJobType.EXPORT_TEI;
    }

    @Override
    public ExportFormat getExportFormat() {
        return ExportFormat.TEI;
    }
}
