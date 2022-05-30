package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.service.mapper.DatedObjectMapper;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobconfig.dto.JobMapper;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.jobeventconfig.dto.JobEventConfigMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.batch.core.JobExecution;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Mapping(source = "config.krameriusJob", target = "krameriusJob")
    @Mapping(source = "jobEvent", target = "jobParametersMap")
    JobEventRunDto toRunDto(JobEvent jobEvent);

    default Map<String, Object> jobEventToJobParameters(JobEvent jobEvent) {
        Map<String, Object> jobParametersMap = new HashMap<>();
        jobParametersMap.put("jobEventId", jobEvent.getId());
        jobParametersMap.put("jobEventName", jobEvent.getJobName());
        jobParametersMap.put("publicationId", jobEvent.getPublicationId());
        jobParametersMap.put("created", Date.from(jobEvent.getCreated()));
        jobParametersMap.put("krameriusJob", jobEvent.getConfig().getKrameriusJob());
        jobParametersMap.putAll(jobEvent.getConfig().getParameters());

        return jobParametersMap;
    }
}
