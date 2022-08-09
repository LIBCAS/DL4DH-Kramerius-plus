package cz.inqool.dl4dh.krameriusplus.service.system.job.config.validators;

import org.springframework.batch.core.JobParametersValidator;

import java.util.Set;

public interface JobEventValidator extends JobParametersValidator {
    Set<ValidatorType> usedIn();
}
