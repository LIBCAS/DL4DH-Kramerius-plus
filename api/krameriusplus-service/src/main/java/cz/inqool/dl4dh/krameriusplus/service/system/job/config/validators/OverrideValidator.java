package cz.inqool.dl4dh.krameriusplus.service.system.job.config.validators;

import cz.inqool.dl4dh.krameriusplus.core.domain.exception.ValidationException;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventStore;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

import static cz.inqool.dl4dh.krameriusplus.core.domain.exception.ValidationException.ErrorCode.ALREADY_EXISTS;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.JOB_EVENT_ID;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.KRAMERIUS_JOB;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.OVERRIDE;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ID;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.validators.ValidatorType.ENRICHMENT;

@Component
public class OverrideValidator implements JobEventValidator {
    private final JobEventStore jobEventStore;

    @Autowired
    public OverrideValidator(JobEventStore jobEventStore) {
        this.jobEventStore = jobEventStore;
    }


    @Override
    public Set<ValidatorType> usedIn() {
        return Set.of(ENRICHMENT);
    }


    @Override
    public void validate(JobParameters parameters) {
        String publicationId = parameters.getString(PUBLICATION_ID);
        String thisJobEventId = parameters.getString(JOB_EVENT_ID);
        KrameriusJob krameriusJob = KrameriusJob.valueOf(parameters.getString(KRAMERIUS_JOB));
        boolean override = Boolean.parseBoolean(parameters.getString(OVERRIDE));

        if (!override && jobEventStore.existsOtherJobs(publicationId, thisJobEventId, krameriusJob)) {
            throw new ValidationException(
                    String.format("Job of type %s for publication %s already exists and 'override' is set to false.",
                            krameriusJob, publicationId), ALREADY_EXISTS);
        }
    }
}
