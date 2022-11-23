package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.dto.DigitalObjectWithPathDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.processor.PathResolvingProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.reader.PublicationMongoReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.writer.ExportPublicationJsonFileWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.EXPORT_PUBLICATION_JSON;

@Component
public class ExportPublicationJsonStepFactory extends FlowStepFactory<Publication, DigitalObjectWithPathDto> {

    private final ExportPublicationJsonFileWriter writer;

    private final PathResolvingProcessor processor;

    private final PublicationMongoReader reader;

    @Autowired
    public ExportPublicationJsonStepFactory(ExportPublicationJsonFileWriter writer,
                                            PathResolvingProcessor processor,
                                            PublicationMongoReader reader) {
        this.writer = writer;
        this.processor = processor;
        this.reader = reader;
    }

    @Override
    protected String getStepName() {
        return EXPORT_PUBLICATION_JSON;
    }

    @Bean(EXPORT_PUBLICATION_JSON)
    @Override
    public Step build() {
        return super.build();
    }

    @Override
    protected int getChunkSize() {
        return 1;
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
