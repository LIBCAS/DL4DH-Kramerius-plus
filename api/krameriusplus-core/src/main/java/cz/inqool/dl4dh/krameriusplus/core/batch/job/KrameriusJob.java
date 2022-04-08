package cz.inqool.dl4dh.krameriusplus.core.batch.job;

import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventQueue;
import lombok.Getter;

public enum KrameriusJob {
    ENRICHING_JOB(JobEventQueue.ENRICHING_QUEUE.getQueueName()),
    EXPORTING_JOB(JobEventQueue.EXPORTING_QUEUE.getQueueName());
//    JSON_EXPORTING_JOB(JobEventQueue.EXPORTING_QUEUE.getQueueName());

    @Getter
    private String queueName;

    KrameriusJob(String queueName) {
        this.queueName = queueName;
    }
}
