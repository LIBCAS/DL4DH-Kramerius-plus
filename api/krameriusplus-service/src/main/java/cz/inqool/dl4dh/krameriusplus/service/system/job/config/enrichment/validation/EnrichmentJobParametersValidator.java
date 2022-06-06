package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.validation;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.stereotype.Component;

@Component
public class EnrichmentJobParametersValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters jobParameters) throws JobParametersInvalidException {
        if (jobParameters == null) {
            throw new JobParametersInvalidException("JobParameters are null.");
        }
        if (jobParameters.getString("publicationId") == null) {
            throw new JobParametersInvalidException("Parameter 'publicationId' missing.");
        }
        if (jobParameters.getString("jobEventId") == null) {
            throw new JobParametersInvalidException("Parameter 'jobEventId' missing.");
        }
    }
}
