package cz.inqool.dl4dh.krameriusplus.core.job.config.export;

import cz.inqool.dl4dh.krameriusplus.api.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.api.export.params.TeiParams;
import cz.inqool.dl4dh.krameriusplus.core.job.config.TeiParamsJsonConverter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Getter
@Setter
@Entity
public class ExportTeiJobConfig extends ExportJobConfig {

    @Lob
    @Column(columnDefinition = "text")
    @Convert(converter = TeiParamsJsonConverter.class)
    private TeiParams teiParams;

    @Override
    public KrameriusJobType getJobType() {
        return KrameriusJobType.EXPORT_TEI;
    }

    @Override
    public ExportFormat getExportFormat() {
        return ExportFormat.TEI;
    }
}
