package cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.request;

import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentRequestDto;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.service.mapper.DatedObjectMapper;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstanceMapper;
import cz.inqool.dl4dh.krameriusplus.corev2.job.config.EnrichmentJobConfigMapper;
import cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.item.EnrichmentRequestItemMapper;
import org.mapstruct.Mapper;

@Mapper(uses = {
        EnrichmentJobConfigMapper.class,
        EnrichmentRequestItemMapper.class,
        KrameriusJobInstanceMapper.class
})
public interface EnrichmentRequestMapper extends DatedObjectMapper<EnrichmentRequest, EnrichmentRequestCreateDto, EnrichmentRequestDto> {
}
