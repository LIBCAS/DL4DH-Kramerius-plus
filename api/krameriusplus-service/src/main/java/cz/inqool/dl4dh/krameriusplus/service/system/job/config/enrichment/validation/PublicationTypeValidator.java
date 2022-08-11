package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.validation;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublishInfo;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.DataProvider;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.validation.JobEventValidator;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.validation.ValidatorType;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;

import java.util.Set;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ID;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.validation.ValidatorType.ENRICHMENT;


public class PublicationTypeValidator implements JobEventValidator {

    private final DataProvider dataProvider;

    public PublicationTypeValidator(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public Set<ValidatorType> usedIn() {
        return Set.of(ENRICHMENT);
    }

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        DigitalObject digitalObject = dataProvider.getDigitalObject(parameters.getString(PUBLICATION_ID));
        if (!(digitalObject instanceof Publication)) {
            throw new JobParametersInvalidException("Given UUID is for a DigitalObject of class "
                    + digitalObject.getClass().getSimpleName() +
                    ", which is not a subclass of Publication");
        }

        Publication publication = (Publication) digitalObject;
        publication.setPublishInfo(new PublishInfo());

        if (publication.isPdf()) {
            throw new JobParametersInvalidException("Given UUID is a Publication in EPUB or PDF format");
        }
    }
}
