package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.batched.steps;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.AbstractStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.batched.components.CleanUpExportTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.CLEAN_UP_EXPORT;

@Component
public class CleanUpExportStepFactory extends AbstractStepFactory {

    private final CleanUpExportTasklet tasklet;

    @Autowired
    public CleanUpExportStepFactory(CleanUpExportTasklet tasklet) {
        this.tasklet = tasklet;
    }

    @Override
    public Step build() {
        return stepBuilderFactory.get(CLEAN_UP_EXPORT)
                .tasklet(tasklet)
                .build();
    }
}
