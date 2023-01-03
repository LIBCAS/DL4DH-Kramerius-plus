package cz.inqool.dl4dh.krameriusplus.core.job.config.export;

import cz.inqool.dl4dh.krameriusplus.api.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.api.export.params.ParamsDto;
import cz.inqool.dl4dh.krameriusplus.core.job.config.JobConfig;
import cz.inqool.dl4dh.krameriusplus.core.job.config.ParamsJsonConverter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "kplus_export_job_config")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class ExportJobConfig extends JobConfig {

    @NotNull
    @Lob
    @Column(columnDefinition = "text")
    @Convert(converter = ParamsJsonConverter.class)
    private ParamsDto params;

    public abstract ExportFormat getExportFormat();
}
