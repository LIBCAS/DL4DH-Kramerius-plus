package cz.inqool.dl4dh.krameriusplus.corev2.job.config;

import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.api.export.params.TeiParamsDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
public class ExportTeiJobConfig extends ExportJobConfig {

    @NotNull
    @Convert(converter = TeiParamsJsonConverter.class)
    private TeiParamsDto teiParams;

    @Override
    public KrameriusJobType getJobType() {
        return KrameriusJobType.EXPORT_TEI;
    }
}
