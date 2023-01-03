package cz.inqool.dl4dh.krameriusplus.corev2.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.KrameriusJobTypeName.EXPORT_ALTO;
import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.EXPORT_ALTO_PAGES_STEP;

@Configuration
public class ExportAltoJobDesigner extends AbstractExportJobDesigner {

    private Step exportAltoStep;

    @Bean(EXPORT_ALTO)
    @Override
    public Job build() {
        return super.build();
    }

    @Override
    public String getJobName() {
        return EXPORT_ALTO;
    }

    @Override
    protected List<Step> getExportSteps() {
        return List.of(exportAltoStep);
    }

    @Autowired
    public void setExportAltoStep(@Qualifier(EXPORT_ALTO_PAGES_STEP) Step exportAltoStep) {
        this.exportAltoStep = exportAltoStep;
    }
}
