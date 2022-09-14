package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.alto;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.ExportJobDesigner;
import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob.EXPORT_ALTO;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.CLEAN_UP_EXPORT;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.CREATE_EXPORT;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.EXPORT_PAGES_ALTO;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.PREPARE_EXPORT_DIRECTORY;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.PREPARE_PUBLICATION_METADATA;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.ZIP_EXPORT;

@Configuration
public class AltoExportJobDesigner extends ExportJobDesigner {

    @Bean
    public Job exportAltoJob() {
        return getJobBuilder()
                .next(stepContainer.getStep(PREPARE_PUBLICATION_METADATA))
                .next(stepContainer.getStep(PREPARE_EXPORT_DIRECTORY))
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
