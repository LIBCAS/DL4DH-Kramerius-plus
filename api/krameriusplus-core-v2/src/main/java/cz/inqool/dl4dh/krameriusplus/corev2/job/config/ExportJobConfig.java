package cz.inqool.dl4dh.krameriusplus.corev2.job.config;

import cz.inqool.dl4dh.krameriusplus.api.export.params.ParamsDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "kplus_export_job_config")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class ExportJobConfig extends JobConfig {

    @Convert(converter = ParamsJsonConverter.class)
    @NotNull
    private ParamsDto params;
}
