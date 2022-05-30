package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent;

import lombok.Getter;

public enum JobEventQueue {
    ENRICHING_QUEUE("enriching"),
    EXPORTING_QUEUE("export");

    @Getter
    private final String queueName;

    JobEventQueue(String queueName) {
        this.queueName = queueName;
    }
}
