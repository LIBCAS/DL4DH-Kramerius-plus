package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.alto.steps;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.PageAndAltoStringDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.ProcessingDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.AbstractStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.alto.components.DownloadPageAltoStringProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.alto.components.ExportPagesAltoWriter;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.CreateFileStructureProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.DownloadPublicationTreeReader;
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

    private final DownloadPublicationTreeReader reader;

    private final ExportPagesAltoWriter writer;

    private final DownloadPageAltoStringProcessor downloadPageAltoStringProcessor;

    private final CreateFileStructureProcessor createFileStructureProcessor;
    @Autowired
    public ExportPagesAltoStepFactory(DownloadPublicationTreeReader reader,
                                      ExportPagesAltoWriter writer,
                                      DownloadPageAltoStringProcessor downloadPageAltoStringProcessor,
                                      CreateFileStructureProcessor createFileStructureProcessor) {
        this.reader = reader;
        this.writer = writer;
        this.downloadPageAltoStringProcessor = downloadPageAltoStringProcessor;
        this.createFileStructureProcessor = createFileStructureProcessor;
    }

    @Override
    protected String getStepName() {
        return EXPORT_PAGES_ALTO;
    }

    private ItemReader<ProcessingDto> getItemReader() {
        return reader;
    }

    private ItemWriter<PageAndAltoStringDto> getItemWriter() {
        return writer;
    }

    private ItemProcessor<ProcessingDto, PageAndAltoStringDto> getItemProcessor() {
        return new CompositeItemProcessorBuilder<ProcessingDto, PageAndAltoStringDto>()
                .delegates(createFileStructureProcessor, downloadPageAltoStringProcessor)
                .build();
    }

    @Override
    public Step build() {
        return getBuilder()
                .<ProcessingDto, PageAndAltoStringDto>chunk(getChunkSize())
                .reader(getItemReader())
                .processor(getItemProcessor())
                .writer(getItemWriter())
                .listener(createFileStructureProcessor)
                .build();
    }

    private int getChunkSize() {
        return 5;
    }
}
