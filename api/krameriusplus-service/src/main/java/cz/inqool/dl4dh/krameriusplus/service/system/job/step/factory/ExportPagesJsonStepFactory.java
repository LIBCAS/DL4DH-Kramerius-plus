package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.dto.DigitalObjectWithPathDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.processor.PathResolvingProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.reader.PageMongoReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.writer.ExportPagesJsonFileWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.EXPORT_PAGES_JSON;

@Component
public class ExportPagesJsonStepFactory extends FlowStepFactory<Page, DigitalObjectWithPathDto> {

    private final PageMongoReader reader;

    private final PathResolvingProcessor processor;

    private final ExportPagesJsonFileWriter writer;

    @Autowired
    public ExportPagesJsonStepFactory(ExportPagesJsonFileWriter writer, PathResolvingProcessor processor, PageMongoReader reader) {
        this.writer = writer;
        this.processor = processor;
        this.reader = reader;
    }

    @Override
    protected String getStepName() {
        return EXPORT_PAGES_JSON;
    }

    @Bean(EXPORT_PAGES_JSON)
    @Override
    public Step build() {
        return super.build();
    }

    @Override
    protected ItemReader<? extends Page> getItemReader() {
        return reader;
    }

    @Override
    protected ItemWriter<? super DigitalObjectWithPathDto> getItemWriter() {
        return writer;
    }

    @Override
    protected ItemProcessor<? super Page, ? extends DigitalObjectWithPathDto> getItemProcessor() {
        return processor;
    }
}
