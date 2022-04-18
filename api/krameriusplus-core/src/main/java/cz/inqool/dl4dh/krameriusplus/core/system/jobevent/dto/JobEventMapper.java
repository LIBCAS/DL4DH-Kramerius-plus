package cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.sql.service.mapper.DatedObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.job.dto.JobMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.create.DownloadKStructureCreateDto;
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

    default JobEvent fromCreateDto(JobEventCreateDto dto) {
        if (dto instanceof DownloadKStructureCreateDto) {
            return fromCreateDto((DownloadKStructureCreateDto) dto);
        } else if (dto instanceof ExportingJobEventCreateDto) {
            return fromCreateDto((ExportingJobEventCreateDto) dto);
        } else {
            throw new IllegalStateException("No mapping for this instance type");
        }
    }

    JobEvent fromDto(JobEventDto dto);

    JobEventDto toDto(JobEvent entity);

    default JobEventDto toDto(JobEvent entity, List<JobExecution> executions) {
        JobEventDto eventDto = toDto(entity);
        eventDto.setExecutions(executions.stream().map(jobMapper::jobExecutionToJobExecutionDto).collect(Collectors.toList()));

        return eventDto;
    }


    JobEvent fromCreateDto(DownloadKStructureCreateDto dto);

    default JobEvent fromCreateDto(ExportingJobEventCreateDto dto) {
        if (dto == null) {
            return null;
        }

        JobEvent jobEvent = new JobEvent();
        jobEvent.setJobName(dto.getJobName());
        jobEvent.setParent(fromDto(dto.getParent()));
        jobEvent.setPublicationId(dto.getPublicationId());
        jobEvent.setKrameriusJob(dto.getKrameriusJob());
        jobEvent.getParameters().put("publicationTitle", dto.getPublicationTitle());
        jobEvent.getParameters().put("params", dto.getParams());
        jobEvent.getParameters().put("exportFormat", dto.getExportFormat());

        return jobEvent;
    }
}
