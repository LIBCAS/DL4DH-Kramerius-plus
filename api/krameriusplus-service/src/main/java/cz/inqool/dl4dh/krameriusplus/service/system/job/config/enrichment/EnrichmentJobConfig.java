package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobConfigBase;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.validation.EnrichmentJobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Base class for configuring enriching jobs
 */
public abstract class EnrichmentJobConfig extends JobConfigBase {

    private EnrichmentJobParametersValidator enrichmentJobParametersValidator;

    public abstract String getJobName();

    public JobBuilder getJobBuilder() {
        return jobBuilderFactory.get(getJobName())
                .validator(enrichmentJobParametersValidator)
                .listener(jobListener)
                .listener(datedObjectWriteListener)
                .incrementer(new RunIdIncrementer());
    }

    @Autowired
    public void setEnrichmentJobValidator(EnrichmentJobParametersValidator enrichmentJobParametersValidator) {
        this.enrichmentJobParametersValidator = enrichmentJobParametersValidator;
    }
}
