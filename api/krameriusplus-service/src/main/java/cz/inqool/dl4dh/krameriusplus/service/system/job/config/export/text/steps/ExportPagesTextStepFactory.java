package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.text.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.PageAndAltoDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.FlowStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.processor.DownloadPageAltoProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.reader.DownloadPageReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.text.components.ExportPagesTextWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.EXPORT_PAGES_TEXT;

@Component
public class ExportPagesTextStepFactory extends FlowStepFactory<Page, PageAndAltoDto> {

    private final DownloadPageReader reader;

    private final ExportPagesTextWriter writer;

    private final DownloadPageAltoProcessor processor;

    @Autowired
    public ExportPagesTextStepFactory(DownloadPageReader reader,
                                      ExportPagesTextWriter writer,
                                      DownloadPageAltoProcessor processor) {
        this.reader = reader;
        this.writer = writer;
        this.processor = processor;
    }

    @Override
    protected String getStepName() {
        return EXPORT_PAGES_TEXT;
    }

    @Override
    protected ItemReader<Page> getItemReader() {
        return reader;
    }

    @Override
    protected ItemWriter<PageAndAltoDto> getItemWriter() {
        return writer;
    }

    @Override
    protected ItemProcessor<Page, PageAndAltoDto> getItemProcessor() {
        return processor;
    }
}
