package cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.sql.service.mapper.DatedObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.dto.JobMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.jobeventconfig.dto.JobEventConfigMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.batch.core.JobExecution;

import java.util.List;

@Mapper(uses = JobEventConfigMapper.class)
public interface JobEventMapper extends DatedObjectMapper<JobEvent, JobEventCreateDto, JobEventDto> {

    JobMapper jobMapper = Mappers.getMapper(JobMapper.class);

    JobEvent fromDto(JobEventDto dto);

    JobEventDto toDto(JobEvent entity);

    default JobEventDto toDto(JobEvent entity, List<JobExecution> executions) {
        JobEventDto eventDto = toDto(entity);
        eventDto.setExecutions(jobMapper.jobExecutionsToDto(executions));

        return eventDto;
    }
}
