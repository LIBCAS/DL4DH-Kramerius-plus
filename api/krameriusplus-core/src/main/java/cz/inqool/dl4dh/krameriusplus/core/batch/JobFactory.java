package cz.inqool.dl4dh.krameriusplus.core.batch;

import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.KrameriusJobTypeName;
import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;

/**
 * Base factory interface for creating Job beans
 */
public interface JobFactory {

    /**
     * Method used to create a Job bean. This method needs to be annotated with {@link Bean} annotation
     * in implementing classes and given a bean name from {@link KrameriusJobTypeName} class.
     */
    Job build();
}
