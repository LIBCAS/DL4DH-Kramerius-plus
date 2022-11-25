package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.dto.DigitalObjectWithPathDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.processor.PathResolvingProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.reader.PageMongoReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.writer.ExportPagesCsvFileWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.EXPORT_PAGES_CSV;

@Component
public class ExportPagesCsvStepFactory extends FlowStepFactory<Page, DigitalObjectWithPathDto> {

    private final PageMongoReader reader;

    private final PathResolvingProcessor processor;

    private final ExportPagesCsvFileWriter writer;

    @Autowired
    public ExportPagesCsvStepFactory(PageMongoReader reader,
                                     PathResolvingProcessor processor,
                                     ExportPagesCsvFileWriter writer) {
        this.reader = reader;
        this.processor = processor;
        this.writer = writer;
    }

    @Override
    protected String getStepName() {
        return EXPORT_PAGES_CSV;
    }

    @Bean(EXPORT_PAGES_CSV)
    @Override
    public Step build() {
        return super.build();
    }

    @Override
    protected ItemReader<Page> getItemReader() {
        return reader;
    }

    @Override
    protected ItemWriter<DigitalObjectWithPathDto> getItemWriter() {
        return writer;
    }

    @Override
    protected ItemProcessor<DigitalObject, DigitalObjectWithPathDto> getItemProcessor() {
        return processor;
    }
}
