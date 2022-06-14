package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.csv.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.PublicationMongoFlowStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.csv.components.ExportPublicationCsvFileWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.EXPORT_PUBLICATION_CSV;

@Component
public class ExportPublicationCsvStepFactory extends PublicationMongoFlowStepFactory {

    private final ExportPublicationCsvFileWriter writer;

    @Autowired
    public ExportPublicationCsvStepFactory(ExportPublicationCsvFileWriter writer) {
        this.writer = writer;
    }

    @Override
    protected String getStepName() {
        return EXPORT_PUBLICATION_CSV;
    }

    @Override
    protected ItemWriter<Publication> getItemWriter() {
        return writer;
    }
}
