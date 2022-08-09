package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.StepContainer;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.VALIDATE_PREREQUISITES;


public abstract class JobConfigBase {

    protected StepContainer stepContainer;

    protected JobListener jobListener;

    protected JobBuilderFactory jobBuilderFactory;
    public SimpleJobBuilder getJobBuilder() {
        return jobBuilderFactory.get(getJobName())
                .listener(jobListener)
                .incrementer(new RunIdIncrementer())
                .validator(getJobParametersValidator())
                .start(stepContainer.getStep(VALIDATE_PREREQUISITES));
    }

    public abstract String getJobName();

    public abstract JobParametersValidator getJobParametersValidator();

    @Autowired
    public void setJobBuilderFactory(JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Autowired
    public void setJobListener(JobListener jobListener) {
        this.jobListener = jobListener;
    }

    @Autowired
    public void setStepContainer(StepContainer stepContainer) {
        this.stepContainer = stepContainer;
    }
}
