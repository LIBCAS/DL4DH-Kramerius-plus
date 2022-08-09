package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.validation;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.validators.JobEventValidator;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.validators.ValidatorType.ENRICHMENT;

@Component
public class EnrichmentValidator implements JobParametersValidator {
    private Set<JobEventValidator> validatorSet;

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        validatorSet = validatorSet.stream()
                .filter(validator -> validator.usedIn().contains(ENRICHMENT))
                .collect(Collectors.toSet());

        for (JobEventValidator validator: validatorSet
        ) {
            validator.validate(parameters);
        }
    }

    @Autowired
    public void setValidatorSet(Set<JobEventValidator> validatorSet) {
        this.validatorSet = validatorSet.stream()
                .filter(validator -> validator.usedIn().contains(ENRICHMENT))
                .collect(Collectors.toSet());

    }
}
