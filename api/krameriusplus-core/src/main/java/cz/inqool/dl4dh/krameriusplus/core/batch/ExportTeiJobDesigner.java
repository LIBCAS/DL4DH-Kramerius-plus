package cz.inqool.dl4dh.krameriusplus.core.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.KrameriusJobTypeName.EXPORT_TEI;
import static cz.inqool.dl4dh.krameriusplus.core.batch.step.KrameriusStep.*;

@Configuration
public class ExportTeiJobDesigner extends AbstractExportJobDesigner {

    private Step exportTeiPrepareStep;

    private Step exportTeiMergePagesStep;

    private Step exportTeiFinishStep;

    @Override
    protected List<Step> getExportSteps() {
        return List.of(exportTeiPrepareStep, exportTeiMergePagesStep, exportTeiFinishStep);
    }

    @Override
    @Bean(EXPORT_TEI)
    public Job build() {
        return super.build();
    }

    @Override
    public String getJobName() {
        return EXPORT_TEI;
    }

    @Autowired
    public void setExportTeiPrepareStep(@Qualifier(EXPORT_TEI_PREPARE_STEP) Step exportTeiPrepareStep) {
        this.exportTeiPrepareStep = exportTeiPrepareStep;
    }

    @Autowired
    public void setExportTeiMergePagesStep(@Qualifier(EXPORT_TEI_MERGE_PAGES_STEP) Step exportTeiMergePagesStep) {
        this.exportTeiMergePagesStep = exportTeiMergePagesStep;
    }

    @Autowired
    public void setExportTeiFinishStep(@Qualifier(EXPORT_TEI_FINISH_STEP) Step exportTeiFinishStep) {
        this.exportTeiFinishStep = exportTeiFinishStep;
    }
}
