package cz.inqool.dl4dh.krameriusplus.service.system.job;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.EXPORT_PAGES_CSV_MASTER;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.EXPORT_PUBLICATION_CSV;

@Configuration
public class CsvExportJobDesigner extends ExportJobDesigner {

    private Step exportPublicationCsvStep;

    private Step exportPagesCsvMasterStep;

    @Bean
    public Job exportCsvJob() {
        return getJobBuilder()
                .next(preparePublicationMetadataStep)
                .next(prepareExportDirectoryStep)
                .next(exportPublicationCsvStep)
                .next(exportPagesCsvMasterStep)
                .next(zipExportStep)
                .next(createExportStep)
                .next(cleanUpExportStep)
                .build();
    }

    @Override
    public String getJobName() {
        return KrameriusJob.EXPORT_CSV.name();
    }

    @Autowired
    public void setExportPublicationCsvStep(@Qualifier(EXPORT_PUBLICATION_CSV) Step exportPublicationCsvStep) {
        this.exportPublicationCsvStep = exportPublicationCsvStep;
    }

    @Autowired
    public void setExportPagesCsvMasterStep(@Qualifier(EXPORT_PAGES_CSV_MASTER) Step exportPagesCsvMasterStep) {
        this.exportPagesCsvMasterStep = exportPagesCsvMasterStep;
    }
}
