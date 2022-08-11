package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.validation;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.validation.JobEventValidator;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.validation.ValidatorType.EXPORT;

@Component
public class ExportValidator implements JobParametersValidator {

    private List<JobEventValidator> validatorList;

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        for (JobEventValidator validator: validatorList) {
            validator.validate(parameters);
        }
    }

    @Autowired
    public void setValidatorList(List<JobEventValidator> validatorList) {
        this.validatorList = validatorList.stream()
                .filter(validator -> validator.usedIn().contains(EXPORT))
                .collect(Collectors.toList());
    }
}
