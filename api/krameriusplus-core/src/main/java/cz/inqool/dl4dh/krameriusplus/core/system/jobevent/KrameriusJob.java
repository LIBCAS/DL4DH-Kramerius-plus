package cz.inqool.dl4dh.krameriusplus.core.system.jobevent;

import lombok.Getter;

import java.util.Set;

public enum KrameriusJob {
    ENRICHMENT_KRAMERIUS(JobEventQueue.ENRICHING_QUEUE.getQueueName()),
    ENRICHMENT_EXTERNAL(JobEventQueue.ENRICHING_QUEUE.getQueueName()),
    ENRICHMENT_NDK(JobEventQueue.ENRICHING_QUEUE.getQueueName()),
    ENRICHMENT_TEI(JobEventQueue.ENRICHING_QUEUE.getQueueName()),

    EXPORT_JSON(JobEventQueue.EXPORTING_QUEUE.getQueueName()),
    EXPORT_CSV(JobEventQueue.EXPORTING_QUEUE.getQueueName()),
    EXPORT_TEI(JobEventQueue.EXPORTING_QUEUE.getQueueName()),
    EXPORT_ALTO(JobEventQueue.EXPORTING_QUEUE.getQueueName()),
    EXPORT_TEXT(JobEventQueue.EXPORTING_QUEUE.getQueueName());

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
