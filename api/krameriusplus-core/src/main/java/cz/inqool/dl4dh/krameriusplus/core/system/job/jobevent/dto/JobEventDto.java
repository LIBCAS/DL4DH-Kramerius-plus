package cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.service.dto.DatedObjectDto;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.dto.JobExecutionDto;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.jobeventconfig.JobEventConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.batch.core.BatchStatus;

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

    private BatchStatus lastExecutionStatus;

    private JobEvent parent;

    private JobEventConfig config;

    private List<JobExecutionDto> executions = new ArrayList<>();
}
