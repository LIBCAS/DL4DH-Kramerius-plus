package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.json.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.PageMongoFlowStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.json.components.ExportPagesJsonFileWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.EXPORT_PAGES_JSON;

@Component
public class ExportPagesJsonStepFactory extends PageMongoFlowStepFactory {

    private final ExportPagesJsonFileWriter writer;

    @Autowired
    public ExportPagesJsonStepFactory(ExportPagesJsonFileWriter writer) {
        this.writer = writer;
    }

    @Override
    protected String getStepName() {
        return EXPORT_PAGES_JSON;
    }

    @Override
    protected ItemWriter<Page> getItemWriter() {
        return writer;
    }
}
