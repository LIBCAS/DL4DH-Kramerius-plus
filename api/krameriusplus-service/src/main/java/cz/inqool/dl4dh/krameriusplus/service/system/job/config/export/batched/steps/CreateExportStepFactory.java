package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.batched.steps;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.AbstractStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.batched.components.CreateExportTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.CREATE_EXPORT;

@Component
public class CreateExportStepFactory extends AbstractStepFactory {

    private final CreateExportTasklet tasklet;

    @Autowired
    public CreateExportStepFactory(CreateExportTasklet tasklet) {
        this.tasklet = tasklet;
    }

    @Override
    public Step build() {
        return stepBuilderFactory.get(CREATE_EXPORT)
                .tasklet(tasklet)
                .build();
    }
}
