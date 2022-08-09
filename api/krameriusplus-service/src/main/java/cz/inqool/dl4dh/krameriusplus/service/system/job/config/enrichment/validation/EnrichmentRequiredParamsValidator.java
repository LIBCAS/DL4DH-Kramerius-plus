package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.validation;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.validators.JobEventValidator;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.validators.ValidatorType;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;

import java.util.HashSet;
import java.util.Set;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.validators.ValidatorType.ENRICHMENT;
import static org.springframework.batch.core.JobParameter.ParameterType.STRING;

public class EnrichmentRequiredParamsValidator implements JobEventValidator {

    @Override
    public Set<ValidatorType> usedIn() {
        return Set.of(ENRICHMENT);
    }

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        Set<String> missingParameters = new HashSet<>();

        ENRICHMENT.requiredParams().forEach(parameterName -> {
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
}
