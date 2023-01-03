package cz.inqool.dl4dh.krameriusplus.core.batch.validator;

import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey;
import cz.inqool.dl4dh.krameriusplus.core.job.validator.ExportValidator;
import cz.inqool.dl4dh.krameriusplus.core.request.export.request.ExportRequest;
import cz.inqool.dl4dh.krameriusplus.core.request.export.request.ExportRequestStore;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ExportRequestParametersValidator implements JobParametersValidator {

    private ExportRequestStore requestStore;

    private Set<ExportValidator> exportValidators;

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        String requestId = parameters.getString(JobParameterKey.EXPORT_REQUEST_ID);

        ExportRequest request = requestStore.findById(requestId)
                .orElseThrow(() -> new MissingObjectException(ExportRequest.class, requestId));

        for (ExportValidator validator : exportValidators) {
            validator.validate(request);
        }
    }

    @Autowired
    public void setRequestStore(ExportRequestStore requestStore) {
        this.requestStore = requestStore;
    }

    @Autowired
    public void setExportValidators(Set<ExportValidator> exportValidators) {
        this.exportValidators = exportValidators;
    }
}
