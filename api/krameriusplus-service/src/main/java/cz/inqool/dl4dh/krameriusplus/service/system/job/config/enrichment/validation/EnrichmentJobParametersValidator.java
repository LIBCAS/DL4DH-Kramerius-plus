package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.validation;

import cz.inqool.dl4dh.krameriusplus.core.domain.exception.KrameriusException;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.ValidationException;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublishInfo;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventStore;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.DataProvider;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobParametersValidatorBase;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

import static cz.inqool.dl4dh.krameriusplus.core.domain.exception.KrameriusException.ErrorCode.NOT_FOUND;
import static cz.inqool.dl4dh.krameriusplus.core.domain.exception.ValidationException.ErrorCode.ALREADY_EXISTS;
import static cz.inqool.dl4dh.krameriusplus.core.domain.exception.ValidationException.ErrorCode.INVALID_PARAMETERS;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.*;

@Component
public class EnrichmentJobParametersValidator extends JobParametersValidatorBase {

    private final JobEventStore jobEventStore;

    private final DataProvider dataProvider;

    @Autowired
    public EnrichmentJobParametersValidator(JobEventStore jobEventStore, DataProvider dataProvider) {
        this.jobEventStore = jobEventStore;
        this.dataProvider = dataProvider;
    }

    @Override
    public Set<String> getMandatoryParameters() {
        return Set.of(PUBLICATION_ID, JOB_EVENT_ID);
    }

    @Override
    public void validate(JobParameters jobParameters) throws JobParametersInvalidException {
        super.validate(jobParameters);

        validateOverride(jobParameters);
        DigitalObject digitalObject = validateUuid(jobParameters);
        validatePublicationType(digitalObject);
    }

    private void validatePublicationType(DigitalObject digitalObject) {
        if (!(digitalObject instanceof Publication)) {
            throw new ValidationException("Given UUID is for a DigitalObject of class " + digitalObject.getClass().getSimpleName() +
                    ", which is not a subclass of Publication", INVALID_PARAMETERS);
        }

        Publication publication = (Publication) digitalObject;
        publication.setPublishInfo(new PublishInfo());

        if (publication.isPdf()) {
            throw new ValidationException("Given UUID is a Publication in EPUB or PDF format", INVALID_PARAMETERS);
        }
    }

    private DigitalObject validateUuid(JobParameters jobParameters) {
        String publicationId = jobParameters.getString(PUBLICATION_ID);

        try {
            return dataProvider.getDigitalObject(publicationId);
        } catch (KrameriusException e) {
            if (e.getErrorCode() == NOT_FOUND) {
                throw new ValidationException("Publication with given UUID was not found", INVALID_PARAMETERS, e);
            }

            throw e;
        }
    }

    private void validateOverride(JobParameters jobParameters) {
        String publicationId = jobParameters.getString(PUBLICATION_ID);
        String thisJobEventId = jobParameters.getString(JOB_EVENT_ID);
        KrameriusJob krameriusJob = KrameriusJob.valueOf(jobParameters.getString(KRAMERIUS_JOB));
        boolean override = Boolean.parseBoolean(jobParameters.getString(OVERRIDE));

        if (!override && jobEventStore.existsOtherJobs(publicationId, thisJobEventId, krameriusJob)) {
            throw new ValidationException(
                    String.format("Job of type %s for publication %s already exists and 'override' is set to false.",
                            krameriusJob, publicationId), ALREADY_EXISTS);
        }
    }
}
