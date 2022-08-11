package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.validation;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.validators.AbstractRequiredParamsValidator;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.validators.ValidatorType;
import org.springframework.stereotype.Component;

import java.util.Set;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.JOB_EVENT_ID;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ID;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.validators.ValidatorType.ENRICHMENT;

@Component
public class EnrichmentRequiredParamsValidator extends AbstractRequiredParamsValidator {

    @Override
    public Set<ValidatorType> usedIn() {
        return Set.of(ENRICHMENT);
    }

    @Override
    public Set<String> getRequiredParams() {
        return Set.of(PUBLICATION_ID, JOB_EVENT_ID);
    }
}
