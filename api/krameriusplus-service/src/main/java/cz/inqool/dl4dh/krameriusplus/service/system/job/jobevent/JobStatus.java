package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent;

import org.springframework.batch.core.BatchStatus;

public enum JobStatus {
    CREATED,
    COMPLETED,
    STARTING,
    STARTED,
    STOPPING,
    STOPPED,
    FAILED,
    ABANDONED,
    UNKNOWN;

    public static JobStatus from(BatchStatus status) {
        return JobStatus.valueOf(status.name());
    }
}
