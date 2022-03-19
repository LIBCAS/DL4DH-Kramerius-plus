package cz.inqool.dl4dh.krameriusplus.core.batch.job.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.BatchStatus;

import java.util.Collection;
import java.util.Date;

@Getter
@Setter
public class JobExecutionDto {

    private Long id;

    private Collection<StepExecutionDto> stepExecutions;

    private BatchStatus status;

    private Date startTime;

    private Date createTime;

    private Date endTime;

    private Date lastUpdated;

    private String exitStatus;

    private String jobConfigurationName;
}
