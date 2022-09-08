package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobConfigBase;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.ExportCompletionListener;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.validation.ExportValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ExportJobConfig extends JobConfigBase {

    private ExportValidator exportValidator;

    private ExportCompletionListener exportCompletionListener;

    public abstract String getJobName();

    @Override
    protected void decorateJobBuilder(JobBuilder jobBuilder) {
        jobBuilder.listener(exportCompletionListener);
        jobBuilder.validator(exportValidator);
    }

    @Autowired
    public void setExportJobParametersValidator(ExportValidator validator) {
        this.exportValidator = validator;
    }

    @Autowired
    public void setExportCompletionListener(ExportCompletionListener exportCompletionListener) {
        this.exportCompletionListener = exportCompletionListener;
    }
}
