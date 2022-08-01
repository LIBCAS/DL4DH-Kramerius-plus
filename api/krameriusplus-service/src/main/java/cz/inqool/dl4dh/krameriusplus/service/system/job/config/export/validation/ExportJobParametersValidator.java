package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.validation;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobParametersValidatorBase;
import org.springframework.stereotype.Component;

import java.util.Set;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.*;

@Component
public class ExportJobParametersValidator extends JobParametersValidatorBase {

    @Override
    public Set<String> getMandatoryParameters() {
        return Set.of(PUBLICATION_ID, JOB_EVENT_ID, PARAMS);
    }
}
