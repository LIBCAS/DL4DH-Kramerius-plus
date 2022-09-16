package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.validation;

import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventStore;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.validation.JobEventValidator;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.validation.ValidatorType;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;

import java.util.Set;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.*;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.validation.ValidatorType.ENRICHMENT;

public class OverrideValidator implements JobEventValidator {

    private final JobEventStore jobEventStore;

    public OverrideValidator(JobEventStore jobEventStore) {
        this.jobEventStore = jobEventStore;
    }


    @Override
    public Set<ValidatorType> usedIn() {
        return Set.of(ENRICHMENT);
    }


    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        String publicationId = parameters.getString(PUBLICATION_ID);
        String thisJobEventId = parameters.getString(JOB_EVENT_ID);
        KrameriusJob krameriusJob = KrameriusJob.valueOf(parameters.getString(KRAMERIUS_JOB));
        boolean override = Boolean.parseBoolean(parameters.getString(OVERRIDE));

        if (!override && jobEventStore.existsOtherJobs(publicationId, thisJobEventId, krameriusJob)) {
            throw new JobParametersInvalidException(String.format("Job of type %s for publication %s already exists and 'override' is set to false.",
                            krameriusJob, publicationId));
        }
    }
}
