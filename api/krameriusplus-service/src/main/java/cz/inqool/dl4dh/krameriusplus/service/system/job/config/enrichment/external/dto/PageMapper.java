package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.dto;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import org.mapstruct.Mapper;

@Mapper
public interface PageMapper {

    EnrichPageFromAltoDto fromPage(Page page);

    Page toPage(EnrichPageFromAltoDto dto);
}
