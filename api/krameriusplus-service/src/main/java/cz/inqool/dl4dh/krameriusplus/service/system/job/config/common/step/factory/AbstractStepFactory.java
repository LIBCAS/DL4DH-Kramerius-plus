package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.listener.StepFailureListener;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

/**
 * Abstract base class for step factories. Autowires stepBuilderFactory for creating steps.
 */
public abstract class AbstractStepFactory implements StepFactory {

    protected StepBuilderFactory stepBuilderFactory;

    protected StepFailureListener stepFailureListener;

    /**
     * Base method for preparing StepBuilder with base configuration. Should be used in every implementation of
     * {@link StepFactory#build()} method.
     *
     * @return StepBuilder with base configuration
     */
    protected StepBuilder getBuilder() {
        StepBuilder stepBuilder = stepBuilderFactory.get(getStepName());

        applyStepExecutionListeners(stepBuilder);

        return stepBuilder;
    }

    private void applyStepExecutionListeners(StepBuilder stepBuilder) {
        stepBuilder.listener(stepFailureListener);

        for (StepExecutionListener listener : getStepExecutionListeners()) {
            stepBuilder.listener(listener);
        }
    }

    /**
     * Returns the name of the step, which can be then used to obtain the given step.
     */
    protected abstract String getStepName();

    /**
     * Method used to define additional StepExecutionListeners for inheritors of this class
     *
     * @return a List of listeners to be used
     */
    protected List<StepExecutionListener> getStepExecutionListeners() {
        return Collections.emptyList();
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Autowired
    public void setStepFailureListener(StepFailureListener stepFailureListener) {
        this.stepFailureListener = stepFailureListener;
    }
}
