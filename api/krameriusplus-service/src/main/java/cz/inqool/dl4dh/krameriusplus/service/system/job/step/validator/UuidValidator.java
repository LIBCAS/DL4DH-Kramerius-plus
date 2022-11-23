package cz.inqool.dl4dh.krameriusplus.service.system.job.step.validator;

import cz.inqool.dl4dh.krameriusplus.core.domain.exception.KrameriusException;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.DataProvider;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;

import java.util.Set;

import static cz.inqool.dl4dh.krameriusplus.core.domain.exception.KrameriusException.ErrorCode.NOT_FOUND;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ID;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.validator.ValidatorType.ENRICHMENT;

public class UuidValidator implements JobEventValidator {

    private final DataProvider dataProvider;

    public UuidValidator(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public Set<ValidatorType> usedIn() {
        return Set.of(ENRICHMENT);
    }

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        String publicationId = parameters.getString(PUBLICATION_ID);

        try {
            dataProvider.getDigitalObject(publicationId);
        } catch (KrameriusException e) {
            if (e.getErrorCode() == NOT_FOUND) {
                throw new JobParametersInvalidException("Publication with given UUID was not found");
            }

            throw e;
        }
    }
}
