package cz.inqool.dl4dh.krameriusplus.service.system.job.config.validators;

import cz.inqool.dl4dh.krameriusplus.core.domain.exception.KrameriusException;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.ValidationException;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.DataProvider;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

import static cz.inqool.dl4dh.krameriusplus.core.domain.exception.KrameriusException.ErrorCode.NOT_FOUND;
import static cz.inqool.dl4dh.krameriusplus.core.domain.exception.ValidationException.ErrorCode.INVALID_PARAMETERS;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ID;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.validators.ValidatorType.ENRICHMENT;

@Component
public class UuidValidator implements JobEventValidator {
    private final DataProvider dataProvider;

    @Autowired
    public UuidValidator(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public Set<ValidatorType> usedIn() {
        return Set.of(ENRICHMENT);
    }

    @Override
    public void validate(JobParameters parameters) {
        String publicationId = parameters.getString(PUBLICATION_ID);

        try {
            dataProvider.getDigitalObject(publicationId);
        } catch (KrameriusException e) {
            if (e.getErrorCode() == NOT_FOUND) {
                throw new ValidationException("Publication with given UUID was not found", INVALID_PARAMETERS, e);
            }

            throw e;
        }
    }
}
