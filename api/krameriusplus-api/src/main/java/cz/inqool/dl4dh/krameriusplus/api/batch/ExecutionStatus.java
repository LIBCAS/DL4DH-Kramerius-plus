package cz.inqool.dl4dh.krameriusplus.api.batch;

import java.util.Set;

public enum ExecutionStatus {
    // custom values
    CREATED,
    ENQUEUED,
    FAILED_FATALLY,
    CANCELLED,

    // Spring Batch values
    COMPLETED,
    STARTING,
    STARTED,
    STOPPING,
    STOPPED,
    FAILED,
    ABANDONED,
    UNKNOWN;

    public boolean isFinished() {
        return Sets.finished.contains(this);
    }

    public boolean isStartable() {
        return Sets.startable.contains(this);
    }

    public static class Sets {
        public static final Set<ExecutionStatus> finished = Set.of(COMPLETED, FAILED, FAILED_FATALLY, ABANDONED, STOPPED, CANCELLED);
        public static final Set<ExecutionStatus> startable = Set.of(CREATED, ENQUEUED, FAILED, STOPPED);
    }
}
