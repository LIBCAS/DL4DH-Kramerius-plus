package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobConfigBase;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.validation.ExportValidator;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ExportJobConfig extends JobConfigBase {

    private ExportValidator exportValidator;

    public abstract String getJobName();

    @Override
    public JobParametersValidator getJobParametersValidator() {
        return exportValidator;
    }

    @Autowired
    public void setExportJobParametersValidator(ExportValidator validator) {
        this.exportValidator = validator;
    }
}
