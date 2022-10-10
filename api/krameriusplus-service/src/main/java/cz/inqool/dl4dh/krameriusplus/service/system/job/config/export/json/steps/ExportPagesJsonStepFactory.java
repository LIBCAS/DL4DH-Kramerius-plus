package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.json.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.DigitalObjectWithPathDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.AbstractStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.reader.PageMongoReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.PathResolvingProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.json.components.ExportPagesJsonFileWriter;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.EXPORT_PAGES_JSON;

@Component
public class ExportPagesJsonStepFactory extends AbstractStepFactory {

    private final ExportPagesJsonFileWriter writer;

    private final PathResolvingProcessor processor;

    private final PageMongoReader reader;

    @Autowired
    public ExportPagesJsonStepFactory(ExportPagesJsonFileWriter writer, PathResolvingProcessor processor, PageMongoReader reader) {
        this.writer = writer;
        this.processor = processor;
        this.reader = reader;
    }

    @Override
    protected String getStepName() {
        return EXPORT_PAGES_JSON;
    }

    @Override
    public Step build() {
        return getBuilder()
                .<Page, DigitalObjectWithPathDto>chunk(5)
                .reader(reader)
                .writer(writer)
                .processor(processor)
                .listener(processor)
                .build();
    }
}
