package cz.inqool.dl4dh.krameriusplus.core.request.enrichment.chain;

import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentChainDto;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstanceMapper;
import org.mapstruct.Mapper;

@Mapper(uses = {
        KrameriusJobInstanceMapper.class
})
public interface EnrichmentChainMapper {

    EnrichmentChainDto toDto(EnrichmentChain entity);
}
