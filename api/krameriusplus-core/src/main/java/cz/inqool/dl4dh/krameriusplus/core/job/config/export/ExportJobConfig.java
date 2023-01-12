package cz.inqool.dl4dh.krameriusplus.core.job.config.export;

import cz.inqool.dl4dh.krameriusplus.api.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.api.export.params.ParamsDto;
import cz.inqool.dl4dh.krameriusplus.core.job.config.JobConfig;
import cz.inqool.dl4dh.krameriusplus.core.job.config.JobParametersMapWrapper;
import cz.inqool.dl4dh.krameriusplus.core.job.config.ParamsJsonConverter;
import cz.inqool.dl4dh.krameriusplus.core.request.export.request.ExportRequest;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey.REQUEST_NAME;

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

    @OneToOne
    private ExportRequest exportRequest;

    @Override
    public JobParametersMapWrapper toJobParametersWrapper() {
        JobParametersMapWrapper jobParametersMapWrapper = super.toJobParametersWrapper();
        jobParametersMapWrapper.putString(REQUEST_NAME, exportRequest.getName());

        return jobParametersMapWrapper;
    }

    public abstract ExportFormat getExportFormat();
}
