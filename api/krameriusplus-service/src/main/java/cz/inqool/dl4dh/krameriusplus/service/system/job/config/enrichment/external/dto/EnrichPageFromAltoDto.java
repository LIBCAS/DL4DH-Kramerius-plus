package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.dto;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.alto.AltoDto;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.lindat.nametag.NameTagMetadata;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.lindat.udpipe.Token;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Dto for processing of pages in enrichment external
 */
@Getter
@Setter
public class EnrichPageFromAltoDto {

    /**
     * Map from and to page
     */
    private String id;

    /**
     * Map from and to page
     */
    private String parentId;

    /**
     * Map only to page, from UDPipeService
     */
    private List<Token> tokens = new ArrayList<>();

    /**
     * Map only to page, from NameTagService
     */
    private NameTagMetadata nameTagMetadata;

    /**
     * From DownloadPageAltoProcessor
     */
    private AltoDto.LayoutDto altoLayout;

    /**
     * From DownloadPageAltoProcessor
     */
    private String content;
}
