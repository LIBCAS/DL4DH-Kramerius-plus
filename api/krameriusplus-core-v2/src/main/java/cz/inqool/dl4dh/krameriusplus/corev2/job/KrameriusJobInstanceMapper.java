package cz.inqool.dl4dh.krameriusplus.corev2.job;

import cz.inqool.dl4dh.krameriusplus.api.batch.job.KrameriusJobInstanceDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Component
public class KrameriusJobInstanceMapper {

    public KrameriusJobInstanceDto toDto(KrameriusJobInstance entity) {
        if (entity == null) {
            return null;
        }

        KrameriusJobInstanceDto krameriusJobInstanceDto = new KrameriusJobInstanceDto();

        krameriusJobInstanceDto.setId(entity.getId());
        krameriusJobInstanceDto.setJobStatus(entity.getExecutionStatus());
        krameriusJobInstanceDto.setJobType(entity.getJobType());
//        krameriusJobInstanceDto.setExecutions(???);

        return krameriusJobInstanceDto;
    }

    public List<KrameriusJobInstanceDto> toDtoList(Map<Long, KrameriusJobInstance> entityMap) {
        return new TreeMap<>(entityMap).values()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * This method should never be used, because we should not need to map DTO to ENTITY for EnrichmentRequest.
     * However, it needs to be declared if we want to use the generic DomainService interface so mapstruct can
     * generate it's mapping implementation for EnrichmentRequest. Otherwise, the project would not compile.
     */
    public Map<Long, KrameriusJobInstance> fromDtoList(List<KrameriusJobInstanceDto> dtos) {
        throw new UnsupportedOperationException("Mapping EnrichmentChainDto to EnrichmentChain is not supported.");
    }
}
