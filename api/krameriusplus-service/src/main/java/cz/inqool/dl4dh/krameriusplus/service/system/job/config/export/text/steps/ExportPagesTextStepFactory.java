package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.text.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.PageAndAltoDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.AbstractStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.processor.DownloadPageAltoProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.reader.DownloadPageReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.FilteringProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.reader.DownloadDigitalObjectReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.PathResolvingProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.text.components.DownloadPageAltoProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.text.components.ExportPagesTextWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.EXPORT_PAGES_TEXT;

@Component
public class ExportPagesTextStepFactory extends AbstractStepFactory {

    private final DownloadDigitalObjectReader reader;

    private final ExportPagesTextWriter writer;

    private final DownloadPageAltoProcessor downloadPagesAltoProcessor;

    private final PathResolvingProcessor pathResolvingProcessor;
    private final FilteringProcessor filteringProcessor;

    private final DownloadPageAltoProcessor processor;

    @Autowired
    public ExportPagesTextStepFactory(DownloadDigitalObjectReader reader,
                                      ExportPagesTextWriter writer,
                                      FilteringProcessor filteringProcessor, DownloadPageAltoProcessor processor) {
                                      DownloadPageAltoProcessor downloadPagesAltoProcessor,
                                      PathResolvingProcessor pathResolvingProcessor) {
        this.reader = reader;
        this.writer = writer;
        this.filteringProcessor = filteringProcessor;
        this.processor = processor;
        this.downloadPagesAltoProcessor = downloadPagesAltoProcessor;
        this.pathResolvingProcessor = pathResolvingProcessor;
    }

    @Override
    protected String getStepName() {
        return EXPORT_PAGES_TEXT;
    }

    private ItemReader<DigitalObject> getItemReader() {
        return reader;
    }

    private ItemWriter<PageAndAltoDto> getItemWriter() {
        return writer;
    }

    private ItemProcessor<Page, PageAndAltoDto> getItemProcessor() {
        return new CompositeItemProcessorBuilder<Page, PageAndAltoDto>()
                .delegates(filteringProcessor, processor).build();
    private ItemProcessor<DigitalObject, PageAndAltoDto> getItemProcessor() {
        return new CompositeItemProcessorBuilder<DigitalObject, PageAndAltoDto>()
                .delegates(pathResolvingProcessor, downloadPagesAltoProcessor)
                .build();
    }

    @Override
    public Step build() {
        return getBuilder()
                .<DigitalObject, PageAndAltoDto>chunk(getChunkSize())
                .reader(getItemReader())
                .processor(getItemProcessor())
                .writer(getItemWriter())
                .listener(pathResolvingProcessor)
                .build();
    }

    private int getChunkSize() {
        return 5;
    }
}
