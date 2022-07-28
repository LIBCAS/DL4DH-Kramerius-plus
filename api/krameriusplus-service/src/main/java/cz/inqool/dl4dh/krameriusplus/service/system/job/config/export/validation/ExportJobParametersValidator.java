package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.validation;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PARAMS;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ID;

@Component
public class ExportJobParametersValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters jobParameters) throws JobParametersInvalidException {
        if (jobParameters == null) {
            throw new JobParametersInvalidException("JobParameters are null.");
        }

        Map<String, Object> invalidParameters = new HashMap<>();

        if (jobParameters.getString(PUBLICATION_ID) == null) {
            invalidParameters.put(PUBLICATION_ID, null);
        }

        if (jobParameters.getString(PARAMS) == null) {
            invalidParameters.put(PARAMS, null);
        }

        if (!invalidParameters.isEmpty()) {
            throw new JobParametersInvalidException("Invalid jobParameters: " + invalidParameters);
        }
    }
}
