package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobConfigBase;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobParametersValidatorBase;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.validation.EnrichmentJobParametersValidator;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Base class for configuring enriching jobs
 */
public abstract class EnrichmentJobConfig extends JobConfigBase {

    private EnrichmentJobParametersValidator enrichmentJobParametersValidator;

    public abstract String getJobName();

    @Override
    public JobParametersValidatorBase getJobParametersValidator() {
        return enrichmentJobParametersValidator;
    }

    @Autowired
    public void setEnrichmentJobValidator(EnrichmentJobParametersValidator enrichmentJobParametersValidator) {
        this.enrichmentJobParametersValidator = enrichmentJobParametersValidator;
    }
}
