package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.dto.PageAndAltoStringDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.processor.DownloadPageAltoStringProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.processor.FilteringProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.processor.PathResolvingProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.reader.DownloadDigitalObjectReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.writer.ExportPagesAltoWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.EXPORT_PAGES_ALTO;

@Component
public class ExportPagesAltoStepFactory extends FlowStepFactory<DigitalObject, PageAndAltoStringDto> {

    private final DownloadDigitalObjectReader reader;

    private final ExportPagesAltoWriter writer;

    private final FilteringProcessor filteringProcessor;

    private final DownloadPageAltoStringProcessor downloadPageAltoStringProcessor;

    private final PathResolvingProcessor pathResolvingProcessor;

    @Autowired
    public ExportPagesAltoStepFactory(DownloadDigitalObjectReader reader, ExportPagesAltoWriter writer,
                                      FilteringProcessor filteringProcessor, DownloadPageAltoStringProcessor downloadPageAltoStringProcessor,
                                      PathResolvingProcessor pathResolvingProcessor) {
        this.reader = reader;
        this.writer = writer;
        this.filteringProcessor = filteringProcessor;
        this.downloadPageAltoStringProcessor = downloadPageAltoStringProcessor;
        this.pathResolvingProcessor = pathResolvingProcessor;
    }

    @Bean(EXPORT_PAGES_ALTO)
    @Override
    public Step build() {
        return super.build();
    }

    @Override
    protected String getStepName() {
        return EXPORT_PAGES_ALTO;
    }

    @Override
    protected ItemReader<DigitalObject> getItemReader() {
        return reader;
    }

    @Override
    protected ItemWriter<PageAndAltoStringDto> getItemWriter() {
        return writer;
    }

    @Override
    protected ItemProcessor<DigitalObject, PageAndAltoStringDto> getItemProcessor() {
        return new CompositeItemProcessorBuilder<DigitalObject, PageAndAltoStringDto>()
                .delegates(filteringProcessor, pathResolvingProcessor, downloadPageAltoStringProcessor)
                .build();
    }
}
