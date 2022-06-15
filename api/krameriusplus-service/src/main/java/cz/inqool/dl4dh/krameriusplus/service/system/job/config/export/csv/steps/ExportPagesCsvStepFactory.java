package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.csv.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.PageMongoFlowStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.csv.components.ExportPagesCsvFileWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.EXPORT_PAGES_CSV;

@Component
public class ExportPagesCsvStepFactory extends PageMongoFlowStepFactory {

    private final ExportPagesCsvFileWriter writer;

    @Autowired
    public ExportPagesCsvStepFactory(ExportPagesCsvFileWriter writer) {
        this.writer = writer;
    }

    @Override
    protected String getStepName() {
        return EXPORT_PAGES_CSV;
    }

    @Override
    protected ItemWriter<Page> getItemWriter() {
        return writer;
    }
}
