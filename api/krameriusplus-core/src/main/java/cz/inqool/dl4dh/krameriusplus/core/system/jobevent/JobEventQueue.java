package cz.inqool.dl4dh.krameriusplus.core.system.jobevent;

import lombok.Getter;

public enum JobEventQueue {
    ENRICHMENT_QUEUE("enrichment"),
    EXPORT_QUEUE("export");

    @Getter
    private final String queueName;

    JobEventQueue(String queueName) {
        this.queueName = queueName;
    }
}
