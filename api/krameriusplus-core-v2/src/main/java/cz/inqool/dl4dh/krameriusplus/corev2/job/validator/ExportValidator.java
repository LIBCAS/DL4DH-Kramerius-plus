package cz.inqool.dl4dh.krameriusplus.corev2.job.validator;

import cz.inqool.dl4dh.krameriusplus.api.ExportFormat;

/**
 * Interface for validating, if the given export request can be created based
 * on existing metadata for given publications.
 */
public interface ExportValidator {

    /**
     * Returns true if this validator allows to create ExportRequest for given publicationId
     */
    boolean canCreate(String publicationId);

    /**
     * Returns true for all the formats that can be validated using this validator
     */
    boolean supports(ExportFormat exportFormat);
}
