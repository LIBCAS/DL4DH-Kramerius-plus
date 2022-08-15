package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.alto.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.PageAndAltoStringDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.FlowStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.processor.DownloadPageAltoStringProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.reader.DownloadPageReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.alto.components.ExportPagesAltoWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.EXPORT_PAGES_ALTO;

@Component
public class ExportPagesAltoStepFactory extends FlowStepFactory<Page, PageAndAltoStringDto> {

    private final DownloadPageReader reader;

    private final ExportPagesAltoWriter writer;

    private final DownloadPageAltoStringProcessor processor;
    @Autowired
    public ExportPagesAltoStepFactory(DownloadPageReader reader,
                                      ExportPagesAltoWriter writer,
                                      DownloadPageAltoStringProcessor processor) {
        this.reader = reader;
        this.writer = writer;
        this.processor = processor;
    }

    @Override
    protected String getStepName() {
        return EXPORT_PAGES_ALTO;
    }

    @Override
    protected ItemReader<Page> getItemReader() {
        return reader;
    }

    @Override
    protected ItemWriter<PageAndAltoStringDto> getItemWriter() {
        return writer;
    }

    @Override
    protected ItemProcessor<Page, PageAndAltoStringDto> getItemProcessor() {
        return processor;
    }
}
