package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.StepContainer;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.listener.DatedObjectWriteListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class JobConfigBase {

    protected StepContainer stepContainer;

    protected JobListener jobListener;

    protected JobBuilderFactory jobBuilderFactory;

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
