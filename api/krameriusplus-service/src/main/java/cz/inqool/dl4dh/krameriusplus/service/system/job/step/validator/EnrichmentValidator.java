package cz.inqool.dl4dh.krameriusplus.service.system.job.step.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.validator.ValidatorType.ENRICHMENT;

@Component
public class EnrichmentValidator implements JobParametersValidator {

    private List<JobEventValidator> validatorList;

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        for (JobEventValidator validator : validatorList) {
            validator.validate(parameters);
        }
    }

    @Autowired
    public void setValidatorList(List<JobEventValidator> validatorList) {
        this.validatorList = validatorList.stream()
                .filter(validator -> validator.usedIn().contains(ENRICHMENT))
                .collect(Collectors.toList());
    }
}
