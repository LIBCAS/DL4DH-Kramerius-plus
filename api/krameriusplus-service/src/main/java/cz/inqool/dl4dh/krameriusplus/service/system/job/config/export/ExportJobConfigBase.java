package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobConfigBase;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ExportJobConfigBase extends JobConfigBase {

    private ExportJobParametersValidator exportJobParametersValidator;

    public abstract String getJobName();

    public JobBuilder getJobBuilder() {
        return jobBuilderFactory.get(getJobName())
                .validator(exportJobParametersValidator)
                .incrementer(new RunIdIncrementer())
                .listener(jobListener);
    }

    @Autowired
    public void setExportJobParametersValidator(ExportJobParametersValidator validator) {
        this.exportJobParametersValidator = validator;
    }
}
