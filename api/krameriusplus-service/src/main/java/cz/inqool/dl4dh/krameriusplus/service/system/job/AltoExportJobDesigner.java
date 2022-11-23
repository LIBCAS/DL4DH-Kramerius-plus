package cz.inqool.dl4dh.krameriusplus.service.system.job;

import cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory.ExportPagesAltoStepFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob.EXPORT_ALTO;

@Configuration
public class AltoExportJobDesigner extends ExportJobDesigner {

    private Step exportPagesAltoStep;

    @Bean
    public Job exportAltoJob() {
        return getJobBuilder()
                .next(preparePublicationMetadataStep)
                .next(prepareExportDirectoryStep)
                .next(exportPagesAltoStep)
                .next(zipExportStep)
                .next(createExportStep)
                .next(cleanUpExportStep)
                .build();
    }

    @Override
    public String getJobName() {
        return EXPORT_ALTO.name();
    }

    @Autowired
    public void setExportPagesAltoStep(ExportPagesAltoStepFactory stepFactory) {
        this.exportPagesAltoStep = stepFactory.build();
    }
}
