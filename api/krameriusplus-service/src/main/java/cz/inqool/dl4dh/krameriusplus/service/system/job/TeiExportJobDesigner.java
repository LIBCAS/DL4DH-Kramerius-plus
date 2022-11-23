package cz.inqool.dl4dh.krameriusplus.service.system.job;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.EXPORT_TEI;

@Configuration
public class TeiExportJobDesigner extends ExportJobDesigner {

    private Step exportTeiStep;

    @Bean
    public Job exportTeiJob() {
        return getJobBuilder()
                .next(preparePublicationMetadataStep)
                .next(prepareExportDirectoryStep)
                .next(exportTeiStep)
                .next(zipExportStep)
                .next(createExportStep)
                .next(cleanUpExportStep)
                .build();
    }

    @Override
    public String getJobName() {
        return KrameriusJob.EXPORT_TEI.name();
    }

    @Autowired
    public void setExportTeiStep(@Qualifier(EXPORT_TEI) Step exportTeiStep) {
        this.exportTeiStep = exportTeiStep;
    }
}
