package cz.inqool.dl4dh.krameriusplus.service.system.jobevent.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class StepExecutionDto {

    private Long id;

    private String stepName;

    private BatchStatus status;

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

    private ExitStatus exitStatus;

    private boolean terminateOnly;

    private int filterCount;

    private List<Throwable> failureExceptions;

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
