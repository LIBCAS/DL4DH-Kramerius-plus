package cz.inqool.dl4dh.krameriusplus.core.batch;

import cz.inqool.dl4dh.krameriusplus.core.batch.step.KrameriusStep;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

public abstract class AbstractExportJobDesigner extends AbstractJobDesigner {

    private Step prepareExportMetadataStep;

    private Step prepareExportDirectoryStep;

    private Step includeChildExportsStep;

    private Step zipDirectoryStep;

    private Step saveExportFileStep;

    private Step cleanUpExportStep;

    protected abstract List<Step> getExportSteps();

    public Job build() {
        var builder = getJobBuilder()
                .start(prepareExportMetadataStep)
                .next(prepareExportDirectoryStep);

        for (Step exportStep : getExportSteps()) {
            builder.next(exportStep);
        }

        return builder
                .next(includeChildExportsStep)
                .next(zipDirectoryStep)
                .next(saveExportFileStep)
                .next(cleanUpExportStep)
                .build();
    }

    @Autowired
    public void setPrepareExportMetadataStep(@Qualifier(KrameriusStep.PREPARE_EXPORT_METADATA_STEP) Step prepareExportMetadataStep) {
        this.prepareExportMetadataStep = prepareExportMetadataStep;
    }

    @Autowired
    public void setPrepareExportDirectoryStep(@Qualifier(KrameriusStep.PREPARE_EXPORT_DIRECTORY_STEP) Step prepareExportDirectoryStep) {
        this.prepareExportDirectoryStep = prepareExportDirectoryStep;
    }

    @Autowired
    public void setIncludeChildExportsStep(@Qualifier(KrameriusStep.INCLUDE_CHILD_EXPORTS_STEP) Step includeChildExportsStep) {
        this.includeChildExportsStep = includeChildExportsStep;
    }

    @Autowired
    public void setZipDirectoryStep(@Qualifier(KrameriusStep.ZIP_DIRECTORY_STEP) Step zipDirectoryStep) {
        this.zipDirectoryStep = zipDirectoryStep;
    }

    @Autowired
    public void setSaveExportFileStep(@Qualifier(KrameriusStep.SAVE_EXPORT_FILE_STEP) Step saveExportFileStep) {
        this.saveExportFileStep = saveExportFileStep;
    }

    @Autowired
    public void setCleanUpExportStep(@Qualifier(KrameriusStep.CLEAN_UP_EXPORT_STEP) Step cleanUpExportStep) {
        this.cleanUpExportStep = cleanUpExportStep;
    }
}
