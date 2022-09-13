package cz.inqool.dl4dh.krameriusplus.service.system.job.enrichmentrequest.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.mapper.OwnedObjectMapper;
import cz.inqool.dl4dh.krameriusplus.service.system.job.enrichmentrequest.EnrichmentRequest;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto.JobPlanMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = JobPlanMapper.class)
public interface EnrichmentRequestMapper extends OwnedObjectMapper<EnrichmentRequest, EnrichmentRequestCreateDto, EnrichmentRequestDto> {

    JobPlanMapper jobPlanMapper = Mappers.getMapper(JobPlanMapper.class);

    default EnrichmentRequestCreateDto fromSimplifiedCreateDto(EnrichmentRequestSimplifiedCreateDto dto) {
        EnrichmentRequestCreateDto result = new EnrichmentRequestCreateDto();
        result.setName(dto.getName());
        result.setJobPlans(jobPlanMapper.fromEnrichmentRequestCreateDto(dto));

        return result;
    }

}
