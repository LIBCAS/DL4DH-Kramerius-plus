package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.validation;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobParameterKey.JOB_EVENT_ID;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobParameterKey.PUBLICATION_ID;

@Component
public class EnrichmentJobParametersValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters jobParameters) throws JobParametersInvalidException {
        if (jobParameters == null) {
            throw new JobParametersInvalidException("JobParameters are null.");
        }
        if (jobParameters.getString(PUBLICATION_ID) == null) {
            throw new JobParametersInvalidException("Parameter '" + PUBLICATION_ID + "' is missing.");
        }
        if (jobParameters.getString(JOB_EVENT_ID) == null) {
            throw new JobParametersInvalidException("Parameter '" + JOB_EVENT_ID + "' is missing.");
        }
    }
}
