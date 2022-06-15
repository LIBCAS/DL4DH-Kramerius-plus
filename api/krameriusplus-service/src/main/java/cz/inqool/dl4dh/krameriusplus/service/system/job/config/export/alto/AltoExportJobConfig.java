package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.alto;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.ExportJobConfigBase;
import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.KrameriusJob.EXPORT_ALTO;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.*;

@Configuration
public class AltoExportJobConfig extends ExportJobConfigBase {

    @Bean
    public Job exportAltoJob() {
        return getJobBuilder()
                .start(stepContainer.getStep(PREPARE_EXPORT_DIRECTORY))
                .next(stepContainer.getStep(EXPORT_PAGES_ALTO))
                .next(stepContainer.getStep(ZIP_EXPORT))
                .next(stepContainer.getStep(CREATE_EXPORT))
                .next(stepContainer.getStep(CLEAN_UP_EXPORT))
                .build();
    }
    @Override
    public String getJobName() {
        return EXPORT_ALTO.name();
    }
}
