package cz.inqool.dl4dh.krameriusplus.api.batch;

public enum ExecutionStatus {
    CREATED,
    COMPLETED,
    STARTING,
    STARTED,
    STOPPING,
    STOPPED,
    FAILED,
    FAILED_FATALLY,
    ABANDONED,
    UNKNOWN;
}
