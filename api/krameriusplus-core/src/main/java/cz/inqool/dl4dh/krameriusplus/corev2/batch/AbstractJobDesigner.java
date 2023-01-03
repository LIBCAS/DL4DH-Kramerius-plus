package cz.inqool.dl4dh.krameriusplus.corev2.batch;

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Base configuration class with default components that need to be in every job.
 */
public abstract class AbstractJobDesigner implements JobFactory {

    protected JobBuilderFactory jobBuilderFactory;

    protected JobBuilder getJobBuilder() {
        JobBuilder jobBuilder = jobBuilderFactory.get(getJobName());

        decorateJobBuilder(jobBuilder);

        return jobBuilder
                .incrementer(new RunIdIncrementer());
    }

    /**
     * Method which allows to add components to JobBuilder before it is built.
     * @param jobBuilder jobBuilder to decorate
     */
    protected void decorateJobBuilder(JobBuilder jobBuilder) {
        // defaults to no-op
    }

    public abstract String getJobName();

    @Autowired
    public void setJobBuilderFactory(JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
    }
}
