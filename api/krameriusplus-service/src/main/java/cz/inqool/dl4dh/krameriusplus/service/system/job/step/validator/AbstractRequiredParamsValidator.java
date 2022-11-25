package cz.inqool.dl4dh.krameriusplus.service.system.job.step.validator;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.batch.core.JobParameter.ParameterType.STRING;

public abstract class AbstractRequiredParamsValidator implements JobEventValidator {

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        Set<String> missingParameters = new HashSet<>();

        getRequiredParams().forEach(parameterName -> {
            JobParameter jobParameter = parameters.getParameters().get(parameterName);
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

    public abstract Set<String> getRequiredParams();
}
