package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.steps;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.AbstractStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.CleanUpChildExportsTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.CLEAN_UP_CHILD_EXPORTS;

@Component
public class CleanUpChildExportsStepFactory extends AbstractStepFactory {

    private final CleanUpChildExportsTasklet tasklet;

    @Autowired
    public CleanUpChildExportsStepFactory(CleanUpChildExportsTasklet tasklet) {
        this.tasklet = tasklet;
    }


    @Override
    protected String getStepName() {
        return CLEAN_UP_CHILD_EXPORTS;
    }

    @Override
    public Step build() {
        return getBuilder()
                .tasklet(tasklet)
                .build();
    }
}
