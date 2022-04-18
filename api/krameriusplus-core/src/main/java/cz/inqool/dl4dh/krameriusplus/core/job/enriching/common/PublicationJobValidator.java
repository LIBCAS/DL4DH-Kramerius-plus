package cz.inqool.dl4dh.krameriusplus.core.job.enriching.common;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.stereotype.Component;

@Component
public class PublicationJobValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters jobParameters) throws JobParametersInvalidException {
        if (jobParameters == null || jobParameters.getString("publicationId") == null) {
            throw new JobParametersInvalidException("Parameter 'publicationId' missing.");
        }
    }
}
