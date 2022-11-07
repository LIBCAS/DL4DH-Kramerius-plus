package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.dto;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import org.springframework.stereotype.Component;

/**
 * Mapper for enrichment external's processing of pages
 */
@Component
public class PageMapper {

    public EnrichPageFromAltoDto fromPage(Page page) {
        EnrichPageFromAltoDto result = new EnrichPageFromAltoDto();

        result.setId(page.getId());
        result.setParentId(page.getParentId());

        return result;
    }

    public Page toPage(EnrichPageFromAltoDto dto) {
        Page result = new Page();

        result.setId(dto.getId());
        result.setParentId(dto.getParentId());
        result.setTokens(dto.getTokens());
        result.setNameTagMetadata(dto.getNameTagMetadata());

        return result;
    }
}
