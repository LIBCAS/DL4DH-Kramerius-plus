package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.text.steps;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.PageAndAltoDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.ProcessingDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.AbstractStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.CreateFileStructureProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.DownloadPublicationTreeReader;
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

    private final DownloadPublicationTreeReader reader;

    private final ExportPagesTextWriter writer;

    private final DownloadPageAltoProcessor downloadPagesAltoProcessor;

    private final CreateFileStructureProcessor createFileStructureProcessor;

    @Autowired
    public ExportPagesTextStepFactory(DownloadPublicationTreeReader reader,
                                      ExportPagesTextWriter writer,
                                      DownloadPageAltoProcessor downloadPagesAltoProcessor,
                                      CreateFileStructureProcessor createFileStructureProcessor) {
        this.reader = reader;
        this.writer = writer;
        this.createFileStructureProcessor = createFileStructureProcessor;
        this.downloadPagesAltoProcessor = downloadPagesAltoProcessor;
    }

    @Override
    protected String getStepName() {
        return EXPORT_PAGES_TEXT;
    }

    private ItemReader<ProcessingDto> getItemReader() {
        return reader;
    }

    private ItemWriter<PageAndAltoDto> getItemWriter() {
        return writer;
    }

    private ItemProcessor<ProcessingDto, PageAndAltoDto> getItemProcessor() {
        return new CompositeItemProcessorBuilder<ProcessingDto, PageAndAltoDto>()
                .delegates(createFileStructureProcessor, downloadPagesAltoProcessor)
                .build();
    }

    @Override
    public Step build() {
        return getBuilder()
                .<ProcessingDto, PageAndAltoDto>chunk(getChunkSize())
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
