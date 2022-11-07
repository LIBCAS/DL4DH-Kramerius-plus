package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.alto;

import cz.inqool.dl4dh.krameriusplus.core.domain.exception.KrameriusException;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.dto.EnrichPageFromAltoDto;

import static cz.inqool.dl4dh.krameriusplus.core.domain.exception.KrameriusException.ErrorCode.MISSING_STREAM;

/**
 * Strategy for handling missing ALTO format, which throws an exception only if all
 * pages are missing ALTO.
 */
public class FailIfAllMissingStrategy implements MissingAltoStrategy {

    private long missingAltoCounter = 0;

    private final long totalNumberOfPages;

    public FailIfAllMissingStrategy(long totalNumberOfPages) {
        this.totalNumberOfPages = totalNumberOfPages;
    }

    @Override
    public EnrichPageFromAltoDto handleMissingAlto(EnrichPageFromAltoDto item) {
        missingAltoCounter++;

        if (missingAltoCounter == totalNumberOfPages) {
            throw new KrameriusException(MISSING_STREAM, "No ALTO format found for every page.");
        } else {
            return item;
        }
    }
}
