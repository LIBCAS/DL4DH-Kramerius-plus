package cz.inqool.dl4dh.krameriusplus.service.system.job.config.validators;

import java.util.HashSet;
import java.util.Set;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.JOB_EVENT_ID;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PARAMS;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ID;

public enum ValidatorType {
    ENRICHMENT,
    EXPORT;

    public Set<String> requiredParams() {
        if (this == ENRICHMENT) {
            return Set.of(PUBLICATION_ID, JOB_EVENT_ID);
        }
        if (this == EXPORT) {
            return Set.of(PUBLICATION_ID, JOB_EVENT_ID, PARAMS);
        }
        else {
            return new HashSet<>();
        }
    }
}
