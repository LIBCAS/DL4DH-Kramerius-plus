package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.listener.StepFailureListener;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract base class for step factories. Autowires stepBuilderFactory for creating steps.
 */
public abstract class AbstractStepFactory implements StepFactory {

    protected StepBuilderFactory stepBuilderFactory;

    protected StepFailureListener stepFailureListener;

//    /**
//     * Method used to create a Step bean. This method needs to be annotated with {@link Bean} annotation
//     * in implementing classes and given a bean name from {@link JobStep} enum.
//     */
//    public abstract Step build();

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
        stepBuilder.listener(createPromotionListener());

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

    private ExecutionContextPromotionListener createPromotionListener() {
        List<String> values = new ArrayList<>();
        Field[] fields = ExecutionContextKey.class.getFields();

        try {
            for (Field field : fields) {
                values.add((String) field.get(null));
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("ExecutionContextKey class has private fields, which cannot be accessed.");
        }


        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
        listener.setKeys(values.toArray(new String[0]));

        return listener;
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
