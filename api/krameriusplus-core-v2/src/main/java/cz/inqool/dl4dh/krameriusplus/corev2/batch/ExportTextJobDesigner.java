package cz.inqool.dl4dh.krameriusplus.corev2.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.KrameriusJobTypeName.EXPORT_TEXT;
import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.EXPORT_TEXT_PAGES_STEP;

@Configuration
public class ExportTextJobDesigner extends AbstractExportJobDesigner {

    private Step exportTextStep;

    @Bean(EXPORT_TEXT)
    @Override
    public Job build() {
        return super.build();
    }

    @Override
    protected List<Step> getExportSteps() {
        return List.of(exportTextStep);
    }

    @Override
    public String getJobName() {
        return EXPORT_TEXT;
    }

    @Autowired
    public void setExportTextStep(@Qualifier(EXPORT_TEXT_PAGES_STEP) Step exportTextStep) {
        this.exportTextStep = exportTextStep;
    }
}
