package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.CommonJobConfig;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.validation.EnrichmentJobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class EnrichmentBaseJobConfig extends CommonJobConfig {

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
