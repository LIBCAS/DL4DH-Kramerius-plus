package cz.inqool.dl4dh.krameriusplus.api.batch.job;

import cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus;
import cz.inqool.dl4dh.krameriusplus.api.batch.step.StepExecutionDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class JobExecutionDto {

    private Long id;

    private Collection<StepExecutionDto> stepExecutions;

    private ExecutionStatus status;

    private Date startTime;

    private Date createTime;

    private Date endTime;

    private Date lastUpdated;

    private String exitStatus;

    private String jobConfigurationName;

    private Map<String, Object> jobParameters = new HashMap<>();

    /**
     * Returns the difference in milliseconds
     */
    public Long getDuration() {
        if (endTime == null || startTime == null) {
            return  null;
        }

        return endTime.getTime() - startTime.getTime();
    }
}
