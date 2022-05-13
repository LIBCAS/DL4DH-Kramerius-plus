package cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig;

import cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.JobEventQueue;
import lombok.Getter;

import java.util.Set;

public enum KrameriusJob {
    DOWNLOAD_K_STRUCTURE(JobEventQueue.ENRICHING_QUEUE.getQueueName()),
    ENRICH_EXTERNAL(JobEventQueue.ENRICHING_QUEUE.getQueueName()),
    ENRICH_NDK(JobEventQueue.ENRICHING_QUEUE.getQueueName()),
    ENRICH_TEI(JobEventQueue.ENRICHING_QUEUE.getQueueName()),
    EXPORT(JobEventQueue.EXPORTING_QUEUE.getQueueName());

    @Getter
    private final String queueName;

    KrameriusJob(String queueName) {
        this.queueName = queueName;
    }

    public static Set<KrameriusJob> getEnrichingJobs() {
        return Set.of(DOWNLOAD_K_STRUCTURE, ENRICH_EXTERNAL, ENRICH_NDK, ENRICH_TEI);
    }

    public static Set<KrameriusJob> getExportingJobs() {
        return Set.of(EXPORT);
    }
}
