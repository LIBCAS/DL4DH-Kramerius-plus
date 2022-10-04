package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.csv.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.DigitalObjectWithPathDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.AbstractStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.reader.PublicationMongoReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.PathResolvingProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.csv.components.ExportPublicationCsvFileWriter;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.EXPORT_PUBLICATION_CSV;

@Component
public class ExportPublicationCsvStepFactory extends AbstractStepFactory {

    private final ExportPublicationCsvFileWriter writer;

    private final PublicationMongoReader reader;

    private final PathResolvingProcessor processor;

    @Autowired
    public ExportPublicationCsvStepFactory(ExportPublicationCsvFileWriter writer,
                                           PublicationMongoReader reader,
                                           PathResolvingProcessor processor) {
        this.writer = writer;
        this.reader = reader;
        this.processor = processor;
    }

    @Override
    protected String getStepName() {
        return EXPORT_PUBLICATION_CSV;
    }

    @Override
    public Step build() {
        return getBuilder()
                .<Publication, DigitalObjectWithPathDto>chunk(1)
                .reader(reader)
                .writer(writer)
                .processor(processor)
                .listener(processor)
                .build();
    }
}
