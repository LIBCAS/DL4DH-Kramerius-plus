package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameter;

import java.util.*;

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

    private ExitStatus exitStatus;

    private String jobConfigurationName;

    private List<Throwable> failureExceptions;

    private Map<String, JobParameter> jobParameters = new HashMap<>();
    /**
     * Returns the difference in milliseconds
     */
    public Long getDuration() {
        if (endTime == null || startTime == null) {
            return  null;
        }

        return endTime.getTime() - startTime.getTime();
    }

    public String getStatus() {
        if (status == null || BatchStatus.STARTING.equals(status)) {
            return "CREATED";
        } else {
            return status.name();
        }
    }
}
