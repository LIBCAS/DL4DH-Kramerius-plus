package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.batched.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.service.system.export.exporter2.ExporterMediator2;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobParameterKey.EXPORT_FORMAT;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobParameterKey.PUBLICATION_TITLE;

@Component
@StepScope
public class PublicationExportFileWriter extends DigitalObjectExportFileWriter<Publication> {

    private String publicationTitle;

    public PublicationExportFileWriter(ExporterMediator2 exporterMediator,
                                       @Value("#{jobParameters['" + EXPORT_FORMAT + "']}") String exportFormat) {
        super(exporterMediator, ExportFormat.fromString(exportFormat));
    }

    @Override
    public void write(List<? extends Publication> items) throws Exception {
        publicationTitle = items.get(0).getTitle();
        super.write(items);
    }

    @AfterStep
    public void setDirectoryToZip(StepExecution stepExecution) {
        stepExecution.getJobExecution().getExecutionContext().put(PUBLICATION_TITLE, publicationTitle);
    }
}
