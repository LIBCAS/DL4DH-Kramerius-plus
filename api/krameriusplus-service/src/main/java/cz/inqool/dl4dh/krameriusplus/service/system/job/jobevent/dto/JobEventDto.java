package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.service.dto.DatedObjectDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobconfig.dto.JobExecutionDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.JobStatus;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.jobeventconfig.JobEventConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class JobEventDto extends DatedObjectDto {

    private String jobName;

    private String publicationId;

    @JsonIgnore
    private Long instanceId;

    @JsonIgnore
    private Long lastExecutionId;

    private JobStatus lastExecutionStatus;

    private JobEvent parent;

    private JobEventConfig config;

    private List<JobExecutionDto> executions = new ArrayList<>();
}
