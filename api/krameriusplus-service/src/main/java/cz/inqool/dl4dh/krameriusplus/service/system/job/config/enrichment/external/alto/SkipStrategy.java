package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.alto;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;

/**
 * Strategy for handling missing ALTO format, which skips the page with missing
 * ALTO and continues processing items
 */
public class SkipStrategy implements MissingAltoStrategy {

    @Override
    public Page handleMissingAlto(Page item) {
        return item;
    }
}
