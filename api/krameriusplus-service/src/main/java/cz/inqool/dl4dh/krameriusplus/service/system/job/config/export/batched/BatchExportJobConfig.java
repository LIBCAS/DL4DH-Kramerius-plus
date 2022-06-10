package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.batched;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobConfigBase;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.*;

@Configuration
public class BatchExportJobConfig extends JobConfigBase {

    private final ExportJobParametersValidator validator;

    private final JobListener listener;

    @Autowired
    public BatchExportJobConfig(ExportJobParametersValidator validator, JobListener listener) {
        this.validator = validator;
        this.listener = listener;
    }

    @Bean
    public Job batchExportingJob() {
        return jobBuilderFactory.get(KrameriusJob.BATCH_EXPORT.name())
                .validator(validator)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                // 0.5. create new folder
                // 1. export publication step
                // 2. export pages step
                // 3. zip step
                // 4. cleanup step
                .start(stepContainer.getStep(PREPARE_EXPORT_DIRECTORY))
                .next(stepContainer.getStep(EXPORT_PUBLICATION))
                .next(stepContainer.getStep(EXPORT_PAGES))
                .next(stepContainer.getStep(ZIP_EXPORT))
                .next(stepContainer.getStep(CREATE_EXPORT))
                .next(stepContainer.getStep(CLEAN_UP_EXPORT))
                .build();
    }
}
