package cz.inqool.dl4dh.krameriusplus.core.system.jobevent;

import lombok.Getter;

public enum JobEventQueue {
    ENRICHING_QUEUE("export"),
    EXPORTING_QUEUE("enriching");

    @Getter
    private String queueName;

    JobEventQueue(String queueName) {
        this.queueName = queueName;
    }
}
