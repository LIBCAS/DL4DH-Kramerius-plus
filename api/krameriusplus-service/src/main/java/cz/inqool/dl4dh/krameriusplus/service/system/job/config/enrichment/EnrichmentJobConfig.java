package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobConfigBase;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.validation.EnrichmentValidator;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Base class for configuring enriching jobs
 */
public abstract class EnrichmentJobConfig extends JobConfigBase {

    private EnrichmentValidator enrichmentValidator;

    public abstract String getJobName();

    @Override
    protected JobBuilder addComponents(JobBuilder jobBuilder) {
        return jobBuilder;
    }

    @Override
    public JobParametersValidator getJobParametersValidator() {
        return enrichmentValidator;
    }

    @Autowired
    public void setEnrichmentJobValidator(EnrichmentValidator enrichmentValidator) {
        this.enrichmentValidator = enrichmentValidator;
    }
}
