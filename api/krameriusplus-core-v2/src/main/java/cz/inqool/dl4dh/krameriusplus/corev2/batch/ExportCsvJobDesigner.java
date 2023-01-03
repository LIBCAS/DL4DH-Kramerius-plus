package cz.inqool.dl4dh.krameriusplus.corev2.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.KrameriusJobTypeName.EXPORT_CSV;
import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.EXPORT_CSV_PAGES_STEP;
import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.EXPORT_CSV_PUBLICATION_STEP;

@Configuration
public class ExportCsvJobDesigner extends AbstractExportJobDesigner {

    private Step exportCsvPublicationStep;

    private Step exportCsvPagesStep;

    @Bean(EXPORT_CSV)
    @Override
    public Job build() {
        return super.build();
    }

    @Override
    protected List<Step> getExportSteps() {
        return List.of(exportCsvPublicationStep, exportCsvPagesStep);
    }

    @Override
    public String getJobName() {
        return EXPORT_CSV;
    }

    @Autowired
    public void setExportCsvPublicationStep(@Qualifier(EXPORT_CSV_PUBLICATION_STEP) Step exportCsvPublicationStep) {
        this.exportCsvPublicationStep = exportCsvPublicationStep;
    }

    @Autowired
    public void setExportCsvPagesStep(@Qualifier(EXPORT_CSV_PAGES_STEP) Step exportCsvPagesStep) {
        this.exportCsvPagesStep = exportCsvPagesStep;
    }
}
