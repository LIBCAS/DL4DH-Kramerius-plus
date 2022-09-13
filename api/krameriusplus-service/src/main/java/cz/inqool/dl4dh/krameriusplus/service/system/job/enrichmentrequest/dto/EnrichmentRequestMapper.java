package cz.inqool.dl4dh.krameriusplus.service.system.job.enrichmentrequest.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.mapper.OwnedObjectMapper;
import cz.inqool.dl4dh.krameriusplus.service.system.job.enrichmentrequest.EnrichmentRequest;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto.JobPlanMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = JobPlanMapper.class)
public interface EnrichmentRequestMapper extends OwnedObjectMapper<EnrichmentRequest, EnrichmentRequestCreateDto, EnrichmentRequestDto> {

    JobPlanMapper jobPlanMapper = Mappers.getMapper(JobPlanMapper.class);

    default EnrichmentRequest fromCreateDto(EnrichmentRequestCreateDto dto) {
        EnrichmentRequest result = new EnrichmentRequest();
        result.setName(dto.getName());
        result.setJobPlans(jobPlanMapper.fromPublicationsToCreateDtoSet(dto.getPublicationIds(), dto.getConfigs()));

        return result;
    }

}
