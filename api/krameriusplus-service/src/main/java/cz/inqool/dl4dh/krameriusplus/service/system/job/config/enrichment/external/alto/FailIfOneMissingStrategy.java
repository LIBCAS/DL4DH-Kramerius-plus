package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.alto;

import cz.inqool.dl4dh.krameriusplus.core.domain.exception.KrameriusException;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.dto.EnrichPageFromAltoDto;

import static cz.inqool.dl4dh.krameriusplus.core.domain.exception.KrameriusException.ErrorCode.MISSING_STREAM;

/**
 * Strategy for handling missing ALTO format, which throws an exception
 */
public class FailIfOneMissingStrategy implements MissingAltoStrategy {

    @Override
    public EnrichPageFromAltoDto handleMissingAlto(EnrichPageFromAltoDto item) {
        throw new KrameriusException(MISSING_STREAM, "Missing ALTO for page '" + item.getId() + "'.");
    }
}
