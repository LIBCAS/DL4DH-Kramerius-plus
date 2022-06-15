package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.csv;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.ExportJobConfigBase;
import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.*;

@Configuration
public class CsvExportJobConfig extends ExportJobConfigBase {

    @Bean
    public Job exportCsvJob() {
        return getJobBuilder()
                .start(stepContainer.getStep(PREPARE_EXPORT_DIRECTORY))
                .next(stepContainer.getStep(EXPORT_PUBLICATION_CSV))
                .next(stepContainer.getStep(EXPORT_PAGES_CSV))
                .next(stepContainer.getStep(ZIP_EXPORT))
                .next(stepContainer.getStep(CREATE_EXPORT))
                .next(stepContainer.getStep(CLEAN_UP_EXPORT))
                .build();
    }

    @Override
    public String getJobName() {
        return KrameriusJob.EXPORT_CSV.name();
    }
}
