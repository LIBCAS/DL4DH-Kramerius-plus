package cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.sql.service.mapper.DatedObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.job.dto.JobMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.create.ExportingJobEventCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.create.JobEventCreateDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.batch.core.JobExecution;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface JobEventMapper extends DatedObjectMapper<JobEvent, JobEventCreateDto, JobEventDto> {

    JobMapper jobMapper = Mappers.getMapper(JobMapper.class);

    JobEvent fromCreateJobEventCreateDto(JobEventCreateDto dto);

    @Override
    default JobEvent fromCreateDto(JobEventCreateDto dto) {
        if (dto instanceof ExportingJobEventCreateDto) {
            return fromCreateDto((ExportingJobEventCreateDto) dto);
        } else {
            return fromCreateJobEventCreateDto(dto);
        }
    }

    JobEvent fromDto(JobEventDto dto);

    JobEventDto toDto(JobEvent entity);

    default JobEventDto toDto(JobEvent entity, List<JobExecution> executions) {
        JobEventDto eventDto = toDto(entity);
        eventDto.setExecutions(executions.stream().map(jobMapper::jobExecutionToJobExecutionDto).collect(Collectors.toList()));

        return eventDto;
    }

    default JobEvent fromCreateDto(ExportingJobEventCreateDto dto) {
        if (dto == null) {
            return null;
        }

        JobEvent jobEvent = fromCreateJobEventCreateDto(dto);
        jobEvent.getParameters().put("publicationTitle", dto.getPublicationTitle());
        jobEvent.getParameters().put("params", dto.getParams());
        jobEvent.getParameters().put("exportFormat", dto.getExportFormat());

        return jobEvent;
    }
}
