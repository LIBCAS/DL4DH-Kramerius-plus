package cz.inqool.dl4dh.krameriusplus.service.system.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob.EXPORT_TEXT;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.EXPORT_PAGES_TEXT;

@Configuration
public class TextExportJobDesigner extends ExportJobDesigner {

    private Step exportPagesTextStep;

    @Bean
    public Job exportTextJob() {
        return getJobBuilder()
                .next(preparePublicationMetadataStep)
                .next(prepareExportDirectoryStep)
                .next(exportPagesTextStep)
                .next(zipExportStep)
                .next(createExportStep)
                .next(cleanUpExportStep)
                .build();
    }

    @Override
    public String getJobName() {
        return EXPORT_TEXT.name();
    }

    @Autowired
    public void setExportPagesTextStep(@Qualifier(EXPORT_PAGES_TEXT) Step exportPagesTextStep) {
        this.exportPagesTextStep = exportPagesTextStep;
    }
}
