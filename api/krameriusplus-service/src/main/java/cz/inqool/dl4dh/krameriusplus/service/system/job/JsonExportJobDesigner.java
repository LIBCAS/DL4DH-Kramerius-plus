package cz.inqool.dl4dh.krameriusplus.service.system.job;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.EXPORT_PAGES_JSON_MASTER;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.EXPORT_PUBLICATION_JSON;

@Configuration
public class JsonExportJobDesigner extends ExportJobDesigner {

    private Step exportPublicationJsonStep;

    private Step exportPagesJsonMasterStep;

    @Bean
    public Job exportJsonJob() {
        return getJobBuilder()
                .next(preparePublicationMetadataStep)
                .next(prepareExportDirectoryStep)
                .next(exportPublicationJsonStep)
                .next(exportPagesJsonMasterStep)
                .next(zipExportStep)
                .next(createExportStep)
                .next(cleanUpExportStep)
                .build();
    }

    @Override
    public String getJobName() {
        return KrameriusJob.EXPORT_JSON.name();
    }

    @Autowired
    public void setExportPublicationJsonStep(@Qualifier(EXPORT_PUBLICATION_JSON) Step exportPublicationJsonStep) {
        this.exportPublicationJsonStep = exportPublicationJsonStep;
    }

    @Autowired
    public void setExportPagesJsonMasterStep(@Qualifier(EXPORT_PAGES_JSON_MASTER) Step exportPagesJsonMasterStep) {
        this.exportPagesJsonMasterStep = exportPagesJsonMasterStep;
    }
}
