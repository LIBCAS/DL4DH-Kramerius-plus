package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.alto;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.components.DownloadPagesAltoProcessor;

public interface MissingAltoStrategy {

    /**
     * Handle a scenario in {@link DownloadPagesAltoProcessor}, when ALTO format is missing. To
     * continue processing, return source object. To halt processing, return null. To fail step, throw
     * exception.
     * @param item source object, which has no ALTO format
     * @return Source object if processing should continue, null otherwise.
     */
    Page handleMissingAlto(Page item);
}
