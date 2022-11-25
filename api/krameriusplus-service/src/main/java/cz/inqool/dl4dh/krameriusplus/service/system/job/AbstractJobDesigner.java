package cz.inqool.dl4dh.krameriusplus.service.system.job;

import cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory.PrerequisiteValidationStepFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Base configuration class with default components that need to be in every job.
 */
public abstract class AbstractJobDesigner {

    private Step prerequisiteValidationStep;

    protected JobBuilderFactory jobBuilderFactory;

    protected SimpleJobBuilder getJobBuilder() {
        JobBuilder jobBuilder = jobBuilderFactory.get(getJobName());

        decorateJobBuilder(jobBuilder);

        return jobBuilder
                .incrementer(new RunIdIncrementer())
                .start(prerequisiteValidationStep);
    }

    protected abstract void decorateJobBuilder(JobBuilder jobBuilder);

    public abstract String getJobName();

    @Autowired
    public void setJobBuilderFactory(JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Autowired
    public void setPrerequisiteValidationStep(PrerequisiteValidationStepFactory prerequisiteValidationStepFactory) {
        this.prerequisiteValidationStep = prerequisiteValidationStepFactory.build();
    }
}
