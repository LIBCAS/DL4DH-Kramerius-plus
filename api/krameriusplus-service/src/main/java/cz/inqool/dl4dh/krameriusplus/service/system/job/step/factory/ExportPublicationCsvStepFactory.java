package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.dto.DigitalObjectWithPathDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.processor.PathResolvingProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.reader.PublicationMongoReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.writer.ExportPublicationCsvFileWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.EXPORT_PUBLICATION_CSV;

@Component
public class ExportPublicationCsvStepFactory extends FlowStepFactory<Publication, DigitalObjectWithPathDto> {

    private final PublicationMongoReader reader;

    private final PathResolvingProcessor processor;

    private final ExportPublicationCsvFileWriter writer;

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

    @Bean(EXPORT_PUBLICATION_CSV)
    @Override
    public Step build() {
        return super.build();
    }

    @Override
    protected ItemReader<? extends Publication> getItemReader() {
        return reader;
    }

    @Override
    protected ItemWriter<? super DigitalObjectWithPathDto> getItemWriter() {
        return writer;
    }

    @Override
    protected ItemProcessor<? super Publication, ? extends DigitalObjectWithPathDto> getItemProcessor() {
        return processor;
    }
}
