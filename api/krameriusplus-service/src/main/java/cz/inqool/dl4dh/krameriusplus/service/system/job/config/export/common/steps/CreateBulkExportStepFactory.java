package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.steps;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.AbstractStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.CreateBulkExportTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.CREATE_BULK_EXPORT;

@Component
public class CreateBulkExportStepFactory extends AbstractStepFactory {

    private final CreateBulkExportTasklet tasklet;

    @Autowired
    public CreateBulkExportStepFactory(CreateBulkExportTasklet tasklet) {
        this.tasklet = tasklet;
    }

    @Override
    protected String getStepName() {
        return CREATE_BULK_EXPORT;
    }

    @Override
    public Step build() {
        return getBuilder()
                .tasklet(tasklet)
                .build();
    }
}
