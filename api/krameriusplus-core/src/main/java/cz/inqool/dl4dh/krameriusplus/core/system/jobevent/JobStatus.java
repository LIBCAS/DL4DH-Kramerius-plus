package cz.inqool.dl4dh.krameriusplus.core.system.jobevent;

public enum JobStatus {
    CREATED,
    ENQUEUED,
    COMPLETED,
    STARTING,
    STARTED,
    STOPPING,
    STOPPED,
    FAILED,
    ABANDONED,
    UNKNOWN;

    public static JobStatus from(String status) {
        return JobStatus.valueOf(status);
    }
}
