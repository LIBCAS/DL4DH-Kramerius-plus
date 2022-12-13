package cz.inqool.dl4dh.krameriusplus.api.batch;

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
}
