package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.dto.PageAndAltoDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.processor.DownloadPageAltoProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.processor.FilteringProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.processor.PathResolvingProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.reader.DownloadDigitalObjectReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.writer.ExportPagesTextWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.EXPORT_PAGES_TEXT;

@Component
public class ExportPagesTextStepFactory extends FlowStepFactory<DigitalObject, PageAndAltoDto> {

    private final DownloadDigitalObjectReader reader;

    private final ExportPagesTextWriter writer;

    private final DownloadPageAltoProcessor downloadPagesAltoProcessor;

    private final PathResolvingProcessor pathResolvingProcessor;

    private final FilteringProcessor filteringProcessor;

    @Autowired
    public ExportPagesTextStepFactory(DownloadDigitalObjectReader reader, ExportPagesTextWriter writer,
                                      DownloadPageAltoProcessor downloadPagesAltoProcessor,
                                      PathResolvingProcessor pathResolvingProcessor,
                                      FilteringProcessor filteringProcessor) {
        this.reader = reader;
        this.writer = writer;
        this.downloadPagesAltoProcessor = downloadPagesAltoProcessor;
        this.pathResolvingProcessor = pathResolvingProcessor;
        this.filteringProcessor = filteringProcessor;
    }

    @Bean(EXPORT_PAGES_TEXT)
    @Override
    public Step build() {
        return super.build();
    }

    @Override
    protected String getStepName() {
        return EXPORT_PAGES_TEXT;
    }

    @Override
    protected ItemReader<DigitalObject> getItemReader() {
        return reader;
    }

    @Override
    protected ItemWriter<PageAndAltoDto> getItemWriter() {
        return writer;
    }

    @Override
    protected ItemProcessor<DigitalObject, PageAndAltoDto> getItemProcessor() {
        return new CompositeItemProcessorBuilder<DigitalObject, PageAndAltoDto>()
                .delegates(filteringProcessor, pathResolvingProcessor, downloadPagesAltoProcessor)
                .build();
    }
}
