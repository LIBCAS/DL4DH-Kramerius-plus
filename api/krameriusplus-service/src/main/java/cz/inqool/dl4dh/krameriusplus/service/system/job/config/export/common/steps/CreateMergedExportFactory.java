package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.steps;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.AbstractStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.CreateMergedExportTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.CREATE_MERGED_EXPORT;

@Component
public class CreateMergedExportFactory extends AbstractStepFactory {

    private final CreateMergedExportTasklet tasklet;

    @Autowired
    public CreateMergedExportFactory(CreateMergedExportTasklet tasklet) {
        this.tasklet = tasklet;
    }

    @Override
    protected String getStepName() {
        return CREATE_MERGED_EXPORT;
    }

    @Override
    public Step build() {
        return getBuilder()
                .tasklet(tasklet)
                .build();
    }
}
