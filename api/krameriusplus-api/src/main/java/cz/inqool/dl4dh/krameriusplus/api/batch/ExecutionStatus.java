package cz.inqool.dl4dh.krameriusplus.api.batch;

import java.util.List;

public enum ExecutionStatus {
    // custom values
    CREATED,
    ENQUEUED,
    FAILED_FATALLY,

    // Spring Batch values
    COMPLETED,
    STARTING,
    STARTED,
    STOPPING,
    STOPPED,
    FAILED,
    ABANDONED,
    UNKNOWN;

    public boolean finished() {
        return List.of(COMPLETED, FAILED, FAILED_FATALLY, ABANDONED).contains(this);
    }
}
