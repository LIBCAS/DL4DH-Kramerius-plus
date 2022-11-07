package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.dto;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import lombok.Getter;
import lombok.Setter;

/**
 * Dto for processing of pages in enrichment external
 */
@Getter
@Setter
public class EnrichPageFromAltoDto {

    private Page page;

    /**
     * From DownloadPageAltoProcessor
     */
    private Alto.Layout altoLayout;

    /**
     * From DownloadPageAltoProcessor
     */
    private String content;

    public EnrichPageFromAltoDto(Page item) {
        this.page = item;
    }
}
