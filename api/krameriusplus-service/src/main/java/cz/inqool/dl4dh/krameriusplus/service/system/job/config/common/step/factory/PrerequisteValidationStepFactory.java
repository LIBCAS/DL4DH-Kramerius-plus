package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobPrerequisitesValidationTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.VALIDATE_PREREQUISITES;

@Component
public class PrerequisteValidationStepFactory extends AbstractStepFactory {
    private final JobPrerequisitesValidationTasklet tasklet;

    @Autowired
    public PrerequisteValidationStepFactory(JobPrerequisitesValidationTasklet tasklet) {
        this.tasklet = tasklet;
    }

    @Override
    protected String getStepName() {
        return VALIDATE_PREREQUISITES;
    }

    @Override
    public Step build() {
        return getBuilder()
                .tasklet(tasklet)
                .build();
    }
}
