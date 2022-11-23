package cz.inqool.dl4dh.krameriusplus.service.system.job.step.alto;

import cz.inqool.dl4dh.krameriusplus.core.domain.exception.KrameriusException;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;

import static cz.inqool.dl4dh.krameriusplus.core.domain.exception.KrameriusException.ErrorCode.MISSING_STREAM;

/**
 * Strategy for handling missing ALTO format, which throws an exception
 */
public class FailIfOneMissingStrategy implements MissingAltoStrategy {

    @Override
    public Page handleMissingAlto(Page item) {
        throw new KrameriusException(MISSING_STREAM, "Missing ALTO for page '" + item.getId() + "'.");
    }
}
