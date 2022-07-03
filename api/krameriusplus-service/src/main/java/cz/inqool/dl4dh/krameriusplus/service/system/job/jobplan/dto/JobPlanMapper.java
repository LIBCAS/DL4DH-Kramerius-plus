package cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.mapper.DatedObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.enrichment.EnrichmentJobConfigDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.enrichmentrequest.dto.EnrichmentRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.enrichmentrequest.dto.EnrichmentRequestSimplifiedCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.JobPlan;
import org.mapstruct.Mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper
public interface JobPlanMapper extends DatedObjectMapper<JobPlan, JobPlanCreateDto, JobPlanDto> {

    default JobPlanCreateDto toCreateDto(String publicationId, String jobName, List<EnrichmentJobConfigDto> configs) {
        JobPlanCreateDto planCreateDto = new JobPlanCreateDto();

        configs.forEach(config -> {
            JobEventCreateDto jobEventCreateDto = new JobEventCreateDto();
            jobEventCreateDto.setPublicationId(publicationId);
            jobEventCreateDto.setJobName(jobName);
            jobEventCreateDto.setConfig(config);

            planCreateDto.getJobs().add(jobEventCreateDto);
        });

        return planCreateDto;
    }

    default Set<JobPlanCreateDto> fromEnrichmentRequestCreateDto(EnrichmentRequestSimplifiedCreateDto dto) {
        Set<JobPlanCreateDto> result = new HashSet<>();
        dto.getPublicationIds().forEach(publicationId -> {
            result.add(toCreateDto(publicationId, dto.getName(), dto.getConfigs()));
        });

        return result;
    }
}
