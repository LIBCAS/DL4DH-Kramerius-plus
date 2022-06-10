package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.batched.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.AbstractStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.reader.PageMongoReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.batched.components.PageExportFileWriter;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.EXPORT_PAGES;

@Component
public class ExportPageStepFactory extends AbstractStepFactory {

    private final PageMongoReader reader;

    private final PageExportFileWriter writer;

    @Autowired
    public ExportPageStepFactory(PageMongoReader reader, PageExportFileWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public Step build() {
        return stepBuilderFactory.get(EXPORT_PAGES)
                .<Page, Page> chunk(1)
                .reader(reader)
                .writer(writer)
                .build();
    }
}
