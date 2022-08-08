package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobConfigBase;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobParametersValidatorBase;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.validation.ExportJobParametersValidator;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ExportJobConfig extends JobConfigBase {

    private ExportJobParametersValidator exportJobParametersValidator;

    public abstract String getJobName();

    @Override
    public JobParametersValidatorBase getJobParametersValidator() {
        return exportJobParametersValidator;
    }

    @Autowired
    public void setExportJobParametersValidator(ExportJobParametersValidator validator) {
        this.exportJobParametersValidator = validator;
    }
}
