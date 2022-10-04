package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.json.steps;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.PageWithPathDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.AbstractStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.PathResolvingPageMongoReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.json.components.ExportPagesJsonFileWriter;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.EXPORT_PAGES_JSON;

@Component
public class ExportPagesJsonStepFactory extends AbstractStepFactory {

    private final ExportPagesJsonFileWriter writer;

    private final PathResolvingPageMongoReader reader;

    @Autowired
    public ExportPagesJsonStepFactory(ExportPagesJsonFileWriter writer, PathResolvingPageMongoReader reader) {
        this.writer = writer;
        this.reader = reader;
    }

    @Override
    protected String getStepName() {
        return EXPORT_PAGES_JSON;
    }

    @Override
    public Step build() {
        return getBuilder()
                .<PageWithPathDto, PageWithPathDto>chunk(5)
                .reader(reader)
                .writer(writer)
                .listener(reader)
                .build();
    }
}
