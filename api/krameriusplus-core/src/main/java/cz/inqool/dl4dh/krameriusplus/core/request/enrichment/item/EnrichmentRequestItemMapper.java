package cz.inqool.dl4dh.krameriusplus.core.request.enrichment.item;

import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentRequestItemDto;
import cz.inqool.dl4dh.krameriusplus.core.request.enrichment.chain.EnrichmentChainMapper;
import org.mapstruct.Mapper;

@Mapper(uses = EnrichmentChainMapper.class)
public interface EnrichmentRequestItemMapper {

    EnrichmentRequestItemDto toDto(EnrichmentRequestItem entity);
}
