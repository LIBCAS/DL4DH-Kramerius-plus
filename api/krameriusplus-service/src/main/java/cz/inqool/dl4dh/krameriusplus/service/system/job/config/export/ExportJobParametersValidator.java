package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobParameterKey.PARAMS;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobParameterKey.PUBLICATION_ID;

@Component
public class ExportJobParametersValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        if (parameters == null) {
            throw new NullPointerException("jobParameters");
        }

        Map<String, Object> invalidParameters = new HashMap<>();

        if (parameters.getString(PUBLICATION_ID) == null) {
            invalidParameters.put(PUBLICATION_ID, null);
        }

        if (parameters.getString(PARAMS) == null) {
            invalidParameters.put(PARAMS, null);
        }

        if (!invalidParameters.isEmpty()) {
            throw new JobParametersInvalidException("Invalid parameters: " + invalidParameters);
        }
    }
}
