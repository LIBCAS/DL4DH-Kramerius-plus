package cz.inqool.dl4dh.krameriusplus.service.system.job;

import cz.inqool.dl4dh.krameriusplus.service.system.job.step.validator.EnrichmentValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Base class for configuring enriching jobs
 */
public abstract class EnrichmentJobDesigner extends AbstractJobDesigner {

    private EnrichmentValidator enrichmentValidator;

    @Override
    protected void decorateJobBuilder(JobBuilder jobBuilder) {
        jobBuilder.validator(enrichmentValidator);
    }

    @Autowired
    public void setEnrichmentJobValidator(EnrichmentValidator enrichmentValidator) {
        this.enrichmentValidator = enrichmentValidator;
    }
}
