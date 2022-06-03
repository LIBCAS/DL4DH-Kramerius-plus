package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step;

import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractStepFactory implements StepFactory {

    protected StepBuilderFactory stepBuilderFactory;

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

}
