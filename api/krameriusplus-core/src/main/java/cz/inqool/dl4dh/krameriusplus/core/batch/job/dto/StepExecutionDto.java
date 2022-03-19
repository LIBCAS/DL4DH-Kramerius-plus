package cz.inqool.dl4dh.krameriusplus.core.batch.job.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.BatchStatus;

import java.util.Date;

@Getter
@Setter
public class StepExecutionDto {

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

    private String exitStatus;

    private boolean terminateOnly;

    private int filterCount;

}
