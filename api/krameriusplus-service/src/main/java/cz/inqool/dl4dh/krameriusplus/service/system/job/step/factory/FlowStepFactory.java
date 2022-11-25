package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.service.system.job.step.listener.DatedObjectWriteListener;
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
public abstract class FlowStepFactory<IN, OUT> extends AbstractStepFactory {

    protected DatedObjectWriteListener writeListener;

    /**
     * This method needs to be called in every subclass to declare a bean from this factory class
     */
    public Step build() {
        return getBuilder()
                .<IN, OUT>chunk(getChunkSize())
                .reader(getItemReader())
                .processor(getItemProcessor())
                .writer(getItemWriter())
                .listener(writeListener)
                .build();
    }

    protected int getChunkSize() {
        return 5;
    }

    protected abstract ItemReader<? extends IN> getItemReader();

    protected abstract ItemWriter<? super OUT> getItemWriter();

    protected ItemProcessor<? super IN, ? extends OUT> getItemProcessor() {
        return null; // defaults to no processor
    }

    @Autowired
    public void setWriteListener(DatedObjectWriteListener writeListener) {
        this.writeListener = writeListener;
    }
}
