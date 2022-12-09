package cz.inqool.dl4dh.krameriusplus.corev2.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.KrameriusJobTypeName.EXPORT_ALTO;

@Configuration
public class ExportAltoJobDesigner extends AbstractExportJobDesigner {

    private Step exportAltoStep;

    @Override
    public String getJobName() {
        return EXPORT_ALTO;
    }

    @Bean(EXPORT_ALTO)
    @Override
    public Job build() {
        return super.build();
    }

    @Override
    protected List<Step> getExportSteps() {
        return List.of(exportAltoStep);
    }

    @Autowired
    public void setExportAltoStep(Step exportAltoStep) {
        this.exportAltoStep = exportAltoStep;
    }
}
