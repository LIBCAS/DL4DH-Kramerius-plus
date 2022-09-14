package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobDesignerBase;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.validation.EnrichmentValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Base class for configuring enriching jobs
 */
public abstract class EnrichmentJobDesigner extends JobDesignerBase {

    private EnrichmentValidator enrichmentValidator;

    public abstract String getJobName();

    @Override
    protected void decorateJobBuilder(JobBuilder jobBuilder) {
        jobBuilder.validator(enrichmentValidator);
    }

    @Autowired
    public void setEnrichmentJobValidator(EnrichmentValidator enrichmentValidator) {
        this.enrichmentValidator = enrichmentValidator;
    }
}
