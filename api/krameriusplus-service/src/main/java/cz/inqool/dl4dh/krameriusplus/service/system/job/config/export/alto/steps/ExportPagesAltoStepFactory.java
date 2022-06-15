package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.alto.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.FlowStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.reader.PageMongoReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.alto.components.ExportPagesAltoWriter;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.EXPORT_PAGES_ALTO;

@Component
public class ExportPagesAltoStepFactory extends FlowStepFactory<Page, Page> {

    private final PageMongoReader reader;

    private final ExportPagesAltoWriter writer;

    @Autowired
    public ExportPagesAltoStepFactory(PageMongoReader reader, ExportPagesAltoWriter writer) {
        this.reader = reader;
        this.writer = writer;
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
    protected ItemWriter<Page> getItemWriter() {
        return writer;
    }
}
