package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.csv.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.DigitalObjectWithPathDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.AbstractStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.reader.PageMongoReader;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.PathResolvingProcessor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.csv.components.ExportPagesCsvFileWriter;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.EXPORT_PAGES_CSV;

@Component
public class ExportPagesCsvStepFactory extends AbstractStepFactory {

    private final PageMongoReader reader;

    private final PathResolvingProcessor pathResolvingProcessor;

    private final ExportPagesCsvFileWriter writer;

    @Autowired
    public ExportPagesCsvStepFactory(PageMongoReader reader,
                                     PathResolvingProcessor pathResolvingProcessor,
                                     ExportPagesCsvFileWriter writer) {
        this.reader = reader;
        this.pathResolvingProcessor = pathResolvingProcessor;
        this.writer = writer;
    }

    @Override
    protected String getStepName() {
        return EXPORT_PAGES_CSV;
    }

    @Override
    public Step build() {
        return getBuilder()
                .<Page, DigitalObjectWithPathDto>chunk(5)
                .reader(reader)
                .processor(pathResolvingProcessor)
                .writer(writer)
                .listener(reader)
                .build();
    }
}
