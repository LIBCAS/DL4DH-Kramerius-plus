package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.csv.steps;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.PageWithPathDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.AbstractStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.PathResolvingPageMongoReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.csv.components.ExportPagesCsvFileWriter;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.EXPORT_PAGES_CSV;

@Component
public class ExportPagesCsvStepFactory extends AbstractStepFactory {

    private final PathResolvingPageMongoReader reader;

    private final ExportPagesCsvFileWriter writer;

    @Autowired
    public ExportPagesCsvStepFactory(PathResolvingPageMongoReader reader, ExportPagesCsvFileWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    protected String getStepName() {
        return EXPORT_PAGES_CSV;
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
