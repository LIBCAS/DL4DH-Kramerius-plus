package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.batched;

import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobParameterKey.*;

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

        if (parameters.getString(EXPORT_FORMAT) == null) {
            invalidParameters.put(EXPORT_FORMAT, null);
        } else if (Arrays.stream(ExportFormat.values())
                .noneMatch(value -> value.name().equals(parameters.getString(EXPORT_FORMAT)))) {
            invalidParameters.put(EXPORT_FORMAT, parameters.getString(EXPORT_FORMAT));
        }

        if (!invalidParameters.isEmpty()) {
            throw new JobParametersInvalidException("Invalid parameters: " + invalidParameters);
        }
    }
}
