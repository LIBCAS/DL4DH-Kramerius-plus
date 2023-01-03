package cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.item;

import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentRequestItemDto;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.chain.EnrichmentChainMapper;
import org.mapstruct.Mapper;

@Mapper(uses = EnrichmentChainMapper.class)
public interface EnrichmentRequestItemMapper {

    EnrichmentRequestItemDto toDto(EnrichmentRequestItem entity);
}
