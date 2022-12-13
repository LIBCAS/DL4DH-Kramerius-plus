package cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.request;

import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentRequestDto;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstanceMapper;
import cz.inqool.dl4dh.krameriusplus.corev2.job.config.enrichment.EnrichmentJobConfigMapper;
import cz.inqool.dl4dh.krameriusplus.corev2.request.RequestMapper;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.item.EnrichmentRequestItemMapper;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        uses = {
                EnrichmentJobConfigMapper.class,
                EnrichmentRequestItemMapper.class,
                KrameriusJobInstanceMapper.class})
public abstract class EnrichmentRequestMapper extends RequestMapper<EnrichmentRequest, EnrichmentRequestCreateDto, EnrichmentRequestDto> {
}
