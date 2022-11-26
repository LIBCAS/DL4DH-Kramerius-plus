package cz.inqool.dl4dh.krameriusplus.api.batch;

public enum JobQueue {
    ENRICHMENT_QUEUE("enrichment"),
    EXPORT_QUEUE("export"),
    DEFAULT_QUEUE("default"), // for fast operations,
    PRIORITY_QUEUE("priority") // for high-priority operations
    ;

    private final String queueName;

    JobQueue(String queueName) {
        this.queueName = queueName;
    }
}
