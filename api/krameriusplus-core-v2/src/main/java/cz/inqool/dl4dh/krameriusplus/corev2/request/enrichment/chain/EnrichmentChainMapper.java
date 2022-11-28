package cz.inqool.dl4dh.krameriusplus.corev2.request.enrichment.chain;

import cz.inqool.dl4dh.krameriusplus.api.enrichment.EnrichmentChainDto;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EnrichmentChainMapper {

    private KrameriusJobInstanceMapper jobInstanceMapper;

    public EnrichmentChainDto toDto(EnrichmentChain entity) {
        EnrichmentChainDto dto = new EnrichmentChainDto();
        dto.setId(entity.getId());
        dto.setPublicationId(entity.getPublicationId());
        dto.setJobs(jobInstanceMapper.toDtoList(entity.getJobs()));

        return dto;
    }

    @Autowired
    public void setJobInstanceMapper(KrameriusJobInstanceMapper jobInstanceMapper) {
        this.jobInstanceMapper = jobInstanceMapper;
    }
}
