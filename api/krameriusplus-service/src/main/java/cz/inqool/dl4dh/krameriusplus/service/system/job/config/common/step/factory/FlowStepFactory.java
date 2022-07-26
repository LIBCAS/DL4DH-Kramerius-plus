package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.object.DomainObject;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.listener.DatedObjectWriteListener;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.listener.StepFailureListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class FlowStepFactory<IN extends DomainObject, OUT extends DomainObject>
        extends AbstractStepFactory {

    protected DatedObjectWriteListener writeListener;

    protected StepFailureListener stepFailureListener;

    /**
     * There's a bug in Spring, which causes that not all subclass of this abstract
     * superclass creates a bean, when this method is annotated with @Bean. Therefore,
     * we need a different way to register beans for every subclass of this class
     */
    public Step build() {
        SimpleStepBuilder<IN, OUT> builder = stepBuilderFactory.get(getStepName())
                .<IN, OUT>chunk(getChunkSize())
                .reader(getItemReader())
                .processor(getItemProcessor())
                .writer(getItemWriter());

        for (StepExecutionListener listener : getStepExecutionListeners()) {
            builder.listener(listener);
        }

        return builder.listener(stepFailureListener).listener(writeListener).build();
    }

    /**
     * Returns the name of the step, which can be then used to obtain the given step.
     */
    protected abstract String getStepName();

    protected int getChunkSize() {
        return 5;
    }

    protected abstract ItemReader<IN> getItemReader();

    protected abstract ItemWriter<OUT> getItemWriter();

    protected ItemProcessor<IN, OUT> getItemProcessor() {
        return null; // defaults to no processor
    }

    /**
     * Method used to define listeners for inheritors of this class
     *
     * @return a List of listeners to be used
     */
    protected List<StepExecutionListener> getStepExecutionListeners() {
        return List.of(new StepExecutionListenerSupport()); // defaults to no-op listener
    }

    @Autowired
    public void setWriteListener(DatedObjectWriteListener writeListener) {
        this.writeListener = writeListener;
    }

    @Autowired
    public void setStepFailureListener(StepFailureListener stepFailureListener) {
        this.stepFailureListener = stepFailureListener;
    }
}
