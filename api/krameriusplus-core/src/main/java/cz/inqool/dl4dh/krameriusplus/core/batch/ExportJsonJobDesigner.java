package cz.inqool.dl4dh.krameriusplus.core.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.KrameriusJobTypeName.EXPORT_JSON;
import static cz.inqool.dl4dh.krameriusplus.core.batch.step.KrameriusStep.EXPORT_JSON_PAGES_STEP;
import static cz.inqool.dl4dh.krameriusplus.core.batch.step.KrameriusStep.EXPORT_JSON_PUBLICATION_STEP;

@Configuration
public class ExportJsonJobDesigner extends AbstractExportJobDesigner {

    private Step exportJsonPublicationStep;

    private Step exportJsonPagesStep;

    @Bean(EXPORT_JSON)
    @Override
    public Job build() {
        return super.build();
    }

    @Override
    protected List<Step> getExportSteps() {
        return List.of(exportJsonPublicationStep, exportJsonPagesStep);
    }

    @Override
    public String getJobName() {
        return EXPORT_JSON;
    }

    @Autowired
    public void setExportJsonPublicationStep(@Qualifier(EXPORT_JSON_PUBLICATION_STEP) Step exportJsonPublicationStep) {
        this.exportJsonPublicationStep = exportJsonPublicationStep;
    }

    @Autowired
    public void setExportJsonPagesStep(@Qualifier(EXPORT_JSON_PAGES_STEP) Step exportJsonPagesStep) {
        this.exportJsonPagesStep = exportJsonPagesStep;
    }
}
