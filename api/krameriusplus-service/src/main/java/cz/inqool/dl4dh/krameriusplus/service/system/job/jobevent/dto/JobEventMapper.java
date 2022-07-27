package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.mapper.DatedObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.JobEventConfigMapper;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.dto.JobMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.batch.core.JobExecution;

import java.util.List;

@Mapper(uses = JobEventConfigMapper.class)
public interface JobEventMapper extends DatedObjectMapper<JobEvent, JobEventCreateDto, JobEventDto> {

    JobMapper jobMapper = Mappers.getMapper(JobMapper.class);

    JobEvent fromDto(JobEventDto dto);

    JobEventDto toDto(JobEvent entity);

    List<JobEventDto> toDtoList(List<JobEvent> jobEvents);

    default JobEventDetailDto toDetailDto(JobEvent entity, List<JobExecution> executions) {
        JobEventDetailDto eventDto = new JobEventDetailDto();
        eventDto.setId(entity.getId());
        eventDto.setCreated(entity.getCreated());
        eventDto.setUpdated(entity.getUpdated());
        eventDto.setDeleted(entity.getDeleted());
        eventDto.setJobName(entity.getJobName());
        eventDto.setPublicationId(entity.getPublicationId());
        eventDto.setInstanceId(entity.getInstanceId());
        eventDto.setDetails(entity.getDetails());
        eventDto.setConfig(entity.getConfig());

        if (entity.getParent() != null) {
            eventDto.setParent(toDto(entity.getParent()));
        }

        eventDto.setExecutions(jobMapper.jobExecutionsToDto(executions));

        return eventDto;
    }

    @Mapping(source = "id", target = "jobEventId")
    JobEventRunDto toRunDto(JobEvent jobEvent);
}
