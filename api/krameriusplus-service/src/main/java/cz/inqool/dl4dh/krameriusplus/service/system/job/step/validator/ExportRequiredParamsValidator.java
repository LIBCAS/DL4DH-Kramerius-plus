package cz.inqool.dl4dh.krameriusplus.service.system.job.step.validator;

import org.springframework.stereotype.Component;

import java.util.Set;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.*;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.validator.ValidatorType.EXPORT;

@Component
public class ExportRequiredParamsValidator extends AbstractRequiredParamsValidator {

    @Override
    public Set<ValidatorType> usedIn() {
        return Set.of(EXPORT);
    }

    @Override
    public Set<String> getRequiredParams() {
        return Set.of(PUBLICATION_ID, JOB_EVENT_ID, PARAMS);
    }
}

