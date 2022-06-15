package cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.mapper.DatedObjectMapper;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto.JobEventCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.jobeventconfig.dto.enrichment.EnrichmentJobConfigDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.JobPlan;
import org.mapstruct.Mapper;

import java.util.List;

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
}
