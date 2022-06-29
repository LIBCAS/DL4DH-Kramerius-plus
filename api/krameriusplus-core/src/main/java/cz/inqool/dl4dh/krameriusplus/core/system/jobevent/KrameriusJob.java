package cz.inqool.dl4dh.krameriusplus.core.system.jobevent;

import lombok.Getter;

import java.util.Set;

public enum KrameriusJob {
    ENRICHMENT_KRAMERIUS(JobEventQueue.ENRICHMENT_QUEUE.getQueueName()),
    ENRICHMENT_EXTERNAL(JobEventQueue.ENRICHMENT_QUEUE.getQueueName()),
    ENRICHMENT_NDK(JobEventQueue.ENRICHMENT_QUEUE.getQueueName()),
    ENRICHMENT_TEI(JobEventQueue.ENRICHMENT_QUEUE.getQueueName()),

    EXPORT_JSON(JobEventQueue.EXPORT_QUEUE.getQueueName()),
    EXPORT_CSV(JobEventQueue.EXPORT_QUEUE.getQueueName()),
    EXPORT_TEI(JobEventQueue.EXPORT_QUEUE.getQueueName()),
    EXPORT_ALTO(JobEventQueue.EXPORT_QUEUE.getQueueName()),
    EXPORT_TEXT(JobEventQueue.EXPORT_QUEUE.getQueueName());

    @Getter
    private final String queueName;

    KrameriusJob(String queueName) {
        this.queueName = queueName;
    }

    public static Set<KrameriusJob> getEnrichingJobs() {
        return Set.of(ENRICHMENT_KRAMERIUS, ENRICHMENT_EXTERNAL, ENRICHMENT_NDK, ENRICHMENT_TEI);
    }

    public static Set<KrameriusJob> getExportingJobs() {
        return Set.of(EXPORT_JSON, EXPORT_CSV, EXPORT_TEI, EXPORT_ALTO, EXPORT_TEXT);
    }
}
