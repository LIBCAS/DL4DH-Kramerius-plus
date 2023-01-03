package cz.inqool.dl4dh.krameriusplus.core.job.validator;

import cz.inqool.dl4dh.krameriusplus.core.request.export.request.ExportRequest;
import org.springframework.batch.core.JobParametersInvalidException;

/**
 * Interface for validating, if the given export request can be created based
 * on existing metadata for given publications.
 */
public interface ExportValidator {

    /**
     * Returns true if this validator allows to create ExportRequest for given publicationId
     */
    void validate(ExportRequest request) throws JobParametersInvalidException;
}
