package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.batch.core.JobParameter.ParameterType.STRING;

public abstract class JobParametersValidatorBase implements JobParametersValidator {

    public abstract Set<String> getMandatoryParameters();

    @Override
    public void validate(JobParameters jobParameters) throws JobParametersInvalidException {
        Set<String> missingParameters = new HashSet<>();

        getMandatoryParameters().forEach(parameterName -> {
            JobParameter jobParameter = jobParameters.getParameters().get(parameterName);
            if (jobParameter == null) {
                missingParameters.add(parameterName);
            } else if (STRING.equals(jobParameter.getType()) &&
                    ((String) jobParameter.getValue()).isBlank()) {
                missingParameters.add(parameterName);
            }
        });

        if (!missingParameters.isEmpty()) {
            throw new JobParametersInvalidException("Missing JobParameters: " + missingParameters);
        }
    }
}
