package cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.chain;

import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentChainDto;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstanceMapper;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Mapper(uses = {
        KrameriusJobInstanceMapper.class
})
public interface EnrichmentChainMapper {

    EnrichmentChainDto toDto(EnrichmentChain entity);
}
