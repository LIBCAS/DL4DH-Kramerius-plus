package cz.inqool.dl4dh.krameriusplus.api.batch.step;

import cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class StepExecutionDto {

    private Long id;

    private String stepName;

    private ExecutionStatus status;

    private int readCount;

    private int writeCount;

    private int commitCount;

    private int rollbackCount;

    private int readSkipCount;

    private int processSkipCount;

    private int writeSkipCount;

    private Date startTime;

    private Date endTime;

    private Date lastUpdated;

    private String exitCode;

    private String exitDescription;

    private boolean terminateOnly;

    private int filterCount;

    private List<StepErrorDto> errors = new ArrayList<>();

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
