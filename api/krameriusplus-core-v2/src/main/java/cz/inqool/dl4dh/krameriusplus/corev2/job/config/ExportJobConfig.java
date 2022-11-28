package cz.inqool.dl4dh.krameriusplus.corev2.job.config;

import cz.inqool.dl4dh.krameriusplus.api.export.params.ParamsDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
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
