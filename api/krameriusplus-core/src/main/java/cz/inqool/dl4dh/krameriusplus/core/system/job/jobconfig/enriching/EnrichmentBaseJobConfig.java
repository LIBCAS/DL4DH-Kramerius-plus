package cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.enriching;

import cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.common.CommonJobConfig;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.common.EnrichmentJobValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;

public abstract class EnrichmentBaseJobConfig extends CommonJobConfig {

    private final EnrichmentJobValidator enrichmentJobValidator;

    protected EnrichmentBaseJobConfig(EnrichmentJobValidator enrichmentJobValidator) {
        this.enrichmentJobValidator = enrichmentJobValidator;
    }

    public abstract String getJobName();

    public JobBuilder getJobBuilder() {
        return jobBuilderFactory.get(getJobName())
                .validator(enrichmentJobValidator)
                .listener(jobListener)
                .incrementer(new RunIdIncrementer());
    }
}
