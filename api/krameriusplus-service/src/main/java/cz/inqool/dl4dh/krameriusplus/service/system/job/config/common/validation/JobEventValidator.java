package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.validation;

import org.springframework.batch.core.JobParametersValidator;

import java.util.Set;

public interface JobEventValidator extends JobParametersValidator {
    Set<ValidatorType> usedIn();
}
