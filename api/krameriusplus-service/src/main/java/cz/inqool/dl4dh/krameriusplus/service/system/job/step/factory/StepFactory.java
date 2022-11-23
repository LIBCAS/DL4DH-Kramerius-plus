package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import org.springframework.batch.core.Step;

/**
 * Interface for creating step instances.
 */
public interface StepFactory {

    /**
     * Method used to build the Step
     */
    Step build();
}
