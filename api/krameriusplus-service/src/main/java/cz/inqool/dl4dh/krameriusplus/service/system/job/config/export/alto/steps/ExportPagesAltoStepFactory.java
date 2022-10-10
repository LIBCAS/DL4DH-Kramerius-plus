package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.alto.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.PageAndAltoStringDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.AbstractStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.reader.DownloadDigitalObjectReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.alto.components.DownloadPageAltoStringProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.alto.components.ExportPagesAltoWriter;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.PathResolvingProcessor;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.EXPORT_PAGES_ALTO;

@Component
public class ExportPagesAltoStepFactory extends AbstractStepFactory {

    private final DownloadDigitalObjectReader reader;

    private final ExportPagesAltoWriter writer;

    private final DownloadPageAltoStringProcessor downloadPageAltoStringProcessor;

    private final PathResolvingProcessor pathResolvingProcessor;

    @Autowired
    public ExportPagesAltoStepFactory(DownloadDigitalObjectReader reader,
                                      ExportPagesAltoWriter writer,
                                      DownloadPageAltoStringProcessor downloadPageAltoStringProcessor,
                                      PathResolvingProcessor pathResolvingProcessor) {
        this.reader = reader;
        this.writer = writer;
        this.downloadPageAltoStringProcessor = downloadPageAltoStringProcessor;
        this.pathResolvingProcessor = pathResolvingProcessor;
    }

    @Override
    protected String getStepName() {
        return EXPORT_PAGES_ALTO;
    }

    private ItemReader<DigitalObject> getItemReader() {
        return reader;
    }

    private ItemWriter<PageAndAltoStringDto> getItemWriter() {
        return writer;
    }

    private ItemProcessor<DigitalObject, PageAndAltoStringDto> getItemProcessor() {
        return new CompositeItemProcessorBuilder<DigitalObject, PageAndAltoStringDto>()
                .delegates(pathResolvingProcessor, downloadPageAltoStringProcessor)
                .build();
    }

    @Override
    public Step build() {
        return getBuilder()
                .<DigitalObject, PageAndAltoStringDto>chunk(getChunkSize())
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
