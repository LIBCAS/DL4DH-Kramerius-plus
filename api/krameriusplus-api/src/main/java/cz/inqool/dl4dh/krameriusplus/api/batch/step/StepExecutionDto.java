package cz.inqool.dl4dh.krameriusplus.api.batch.step;

import cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class StepExecutionDto {

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

    private String exitStatus;

    private boolean terminateOnly;

    private int filterCount;

    private StepRunReportDto report;

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
