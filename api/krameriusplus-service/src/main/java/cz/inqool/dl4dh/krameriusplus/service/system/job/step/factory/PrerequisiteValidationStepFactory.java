package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.service.system.job.step.tasklet.JobPrerequisitesValidationTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.VALIDATE_PREREQUISITES;

@Component
public class PrerequisiteValidationStepFactory extends AbstractStepFactory {
    private final JobPrerequisitesValidationTasklet tasklet;

    @Autowired
    public PrerequisiteValidationStepFactory(JobPrerequisitesValidationTasklet tasklet) {
        this.tasklet = tasklet;
    }

    @Bean(VALIDATE_PREREQUISITES)
    @Override
    public Step build() {
        return getBuilder()
                .tasklet(tasklet)
                .build();
    }

    @Override
    protected String getStepName() {
        return VALIDATE_PREREQUISITES;
    }

}
