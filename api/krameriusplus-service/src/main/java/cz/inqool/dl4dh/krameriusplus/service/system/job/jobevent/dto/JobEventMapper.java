package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.mapper.DatedObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.JobEventConfigMapper;
import cz.inqool.dl4dh.krameriusplus.core.utils.JsonUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper(uses = JobEventConfigMapper.class)
public interface JobEventMapper extends DatedObjectMapper<JobEvent, JobEventCreateDto, JobEventDto> {

    JobMapper jobMapper = Mappers.getMapper(JobMapper.class);

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
        eventDto.setOwner(entity.getOwner());

        if (entity.getParent() != null) {
            eventDto.setParent(toDto(entity.getParent()));
        }

        eventDto.setExecutions(jobMapper.jobExecutionsToDto(executions));

        return eventDto;
    }

    default JobParameters toJobParameters(JobEvent jobEvent) {
        Map<String, Object> jobParametersMap = jobEvent.toJobParametersMap();

        JobParametersBuilder builder = new JobParametersBuilder();

        for (Map.Entry<String, Object> entry : jobParametersMap.entrySet()) {
            if (entry.getValue() instanceof String) {
                builder.addString(entry.getKey(), (String) entry.getValue());
            } else if (entry.getValue() instanceof Date) {
                builder.addDate(entry.getKey(), (Date) entry.getValue());
            } else if (entry.getValue() instanceof Long) {
                builder.addLong(entry.getKey(), (Long) entry.getValue());
            } else if (entry.getValue() instanceof Double) {
                builder.addDouble(entry.getKey(), (Double) entry.getValue());
            } else if (entry.getValue() instanceof Enum<?>) {
                builder.addString(entry.getKey(), ((Enum<?>) entry.getValue()).name());
            } else {
                builder.addString(entry.getKey(), JsonUtils.toJsonString(entry.getValue()));
            }
        }

        return builder.toJobParameters();
    }
}
