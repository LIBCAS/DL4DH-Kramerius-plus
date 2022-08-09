package cz.inqool.dl4dh.krameriusplus.service.system.job.config.validators;

import cz.inqool.dl4dh.krameriusplus.core.domain.exception.ValidationException;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublishInfo;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.DataProvider;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

import static cz.inqool.dl4dh.krameriusplus.core.domain.exception.ValidationException.ErrorCode.INVALID_PARAMETERS;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ID;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.validators.ValidatorType.ENRICHMENT;


@Component
public class PublicationTypeValidator implements JobEventValidator {
    private final DataProvider dataProvider;

    @Autowired
    public PublicationTypeValidator(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public Set<ValidatorType> usedIn() {
        return Set.of(ENRICHMENT);
    }

    @Override
    public void validate(JobParameters parameters) {
        DigitalObject digitalObject = dataProvider.getDigitalObject(parameters.getString(PUBLICATION_ID));
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
}
