package cz.inqool.dl4dh.krameriusplus.service.system.job;

import cz.inqool.dl4dh.krameriusplus.service.system.job.step.listener.ExportCompletionListener;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.validator.ExportValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.*;

public abstract class ExportJobDesigner extends AbstractJobDesigner {

    protected Step preparePublicationMetadataStep;

    protected Step prepareExportDirectoryStep;

    protected Step zipExportStep;

    protected Step createExportStep;

    protected Step cleanUpExportStep;

    private ExportValidator exportValidator;

    private ExportCompletionListener exportCompletionListener;

    @Override
    protected void decorateJobBuilder(JobBuilder jobBuilder) {
        jobBuilder.listener(exportCompletionListener);
        jobBuilder.validator(exportValidator);
    }

    @Autowired
    public void setPreparePublicationMetadataStep(@Qualifier(PREPARE_PUBLICATION_METADATA) Step preparePublicationMetadataStep) {
        this.preparePublicationMetadataStep = preparePublicationMetadataStep;
    }

    @Autowired
    public void setPrepareExportDirectoryStep(@Qualifier(PREPARE_EXPORT_DIRECTORY) Step prepareExportDirectoryStep) {
        this.prepareExportDirectoryStep = prepareExportDirectoryStep;
    }

    @Autowired
    public void setZipExportStep(@Qualifier(ZIP_EXPORT) Step zipExportStep) {
        this.zipExportStep = zipExportStep;
    }

    @Autowired
    public void setCreateExportStep(@Qualifier(CREATE_EXPORT) Step createExportStep) {
        this.createExportStep = createExportStep;
    }

    @Autowired
    public void setCleanUpExportStep(@Qualifier(CLEAN_UP_EXPORT) Step cleanUpExportStep) {
        this.cleanUpExportStep = cleanUpExportStep;
    }

    @Autowired
    public void setExportValidator(ExportValidator exportValidator) {
        this.exportValidator = exportValidator;
    }

    @Autowired
    public void setExportCompletionListener(ExportCompletionListener exportCompletionListener) {
        this.exportCompletionListener = exportCompletionListener;
    }
}
