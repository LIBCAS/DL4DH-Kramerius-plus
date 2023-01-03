package cz.inqool.dl4dh.krameriusplus.core.request.enrichment.request;

import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentRequestDto;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstanceMapper;
import cz.inqool.dl4dh.krameriusplus.core.job.config.enrichment.EnrichmentJobConfigMapper;
import cz.inqool.dl4dh.krameriusplus.core.request.RequestMapper;
import cz.inqool.dl4dh.krameriusplus.core.request.enrichment.item.EnrichmentRequestItemMapper;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;

@Mapper(collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        uses = {
                EnrichmentJobConfigMapper.class,
                EnrichmentRequestItemMapper.class,
                KrameriusJobInstanceMapper.class})
public abstract class EnrichmentRequestMapper extends RequestMapper<EnrichmentRequest, EnrichmentRequestCreateDto, EnrichmentRequestDto> {
}
