package cz.inqool.dl4dh.krameriusplus.core.job.config.export;

import cz.inqool.dl4dh.krameriusplus.api.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.api.export.params.ParamsDto;
import cz.inqool.dl4dh.krameriusplus.core.job.config.JobConfigView;
import cz.inqool.dl4dh.krameriusplus.core.job.config.ParamsJsonConverter;
import cz.inqool.dl4dh.krameriusplus.core.request.export.request.ExportRequestView;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "kplus_export_job_config")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        discriminatorType = DiscriminatorType.STRING,
        name = "dtype",
        columnDefinition = "varchar(255)"
)
public abstract class ExportJobConfigView extends JobConfigView {

    @NotNull
    @Lob
    @Column(columnDefinition = "text")
    @Convert(converter = ParamsJsonConverter.class)
    private ParamsDto params;

    @OneToOne
    private ExportRequestView exportRequest;

    public abstract ExportFormat getExportFormat();
}
