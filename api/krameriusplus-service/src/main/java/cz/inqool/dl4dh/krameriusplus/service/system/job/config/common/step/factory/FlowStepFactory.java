package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.StepContainer;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.listener.DatedObjectWriteListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Abstract StepFactory for Steps with {@link ItemReader<IN>}, optional {@link ItemProcessor<IN, OUT>} and
 * {@link ItemWriter<OUT>}
 * @param <IN>
 * @param <OUT>
 */
public abstract class FlowStepFactory<IN, OUT>
        extends AbstractStepFactory {

    protected DatedObjectWriteListener writeListener;

    /**
     * There's a bug in Spring, which causes that not all subclass of this abstract
     * superclass creates a bean, when this method is annotated with @Bean. Therefore,
     * we need a different way to register beans for every subclass of this class.
     * <p>
     * This method is called in {@link StepContainer}, which is responsible for creating and holding a collection
     * of Steps as beans
     */
    public Step build() {
        return getBuilder()
                .<IN, OUT>chunk(getChunkSize())
                .reader(getItemReader())
                .processor(getItemProcessor())
                .writer(getItemWriter())
                .listener(writeListener) // watch out for which method is called, do not use listener(Object listener) method
                .build();
    }

    protected int getChunkSize() {
        return 5;
    }

    protected abstract ItemReader<IN> getItemReader();

    protected abstract ItemWriter<OUT> getItemWriter();

    protected ItemProcessor<IN, OUT> getItemProcessor() {
        return null; // defaults to no processor
    }

    @Autowired
    public void setWriteListener(DatedObjectWriteListener writeListener) {
        this.writeListener = writeListener;
    }
}
