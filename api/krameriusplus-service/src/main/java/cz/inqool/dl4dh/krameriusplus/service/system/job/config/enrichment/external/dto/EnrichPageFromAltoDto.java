package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.dto;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.alto.AltoDto;
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
    private AltoDto.LayoutDto altoLayout;

    /**
     * From DownloadPageAltoProcessor
     */
    private String content;

    public EnrichPageFromAltoDto(Page item) {
        this.page = item;
    }
}
