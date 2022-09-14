package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.StepContainer;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.VALIDATE_PREREQUISITES;


public abstract class JobConfigBase {

    protected StepContainer stepContainer;

    protected JobBuilderFactory jobBuilderFactory;

    public SimpleJobBuilder getJobBuilder() {
        JobBuilder jobBuilder = jobBuilderFactory.get(getJobName());

        decorateJobBuilder(jobBuilder);

        return jobBuilder
                .incrementer(new RunIdIncrementer())
                .start(stepContainer.getStep(VALIDATE_PREREQUISITES));
    }

    protected abstract void decorateJobBuilder(JobBuilder jobBuilder);

    public abstract String getJobName();

    @Autowired
    public void setJobBuilderFactory(JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Autowired
    public void setStepContainer(StepContainer stepContainer) {
        this.stepContainer = stepContainer;
    }
}
