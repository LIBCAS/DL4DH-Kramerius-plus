package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.validation;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.validation.AbstractRequiredParamsValidator;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.validation.ValidatorType;
import org.springframework.stereotype.Component;

import java.util.Set;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.JOB_EVENT_ID;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PARAMS;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ID;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.validation.ValidatorType.EXPORT;

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

