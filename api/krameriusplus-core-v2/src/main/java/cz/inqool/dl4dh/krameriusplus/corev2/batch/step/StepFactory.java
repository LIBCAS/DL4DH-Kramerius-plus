package cz.inqool.dl4dh.krameriusplus.corev2.batch.step;

import org.springframework.batch.core.Step;
import org.springframework.context.annotation.Bean;

/**
 * Interface for creating step instances.
 */
public interface StepFactory {

    /**
     * Method used to create a Step bean. This method needs to be annotated with {@link Bean} annotation
     * in implementing classes and given a bean name from {@link KrameriusStep} classa.
     */
    Step build();
}
