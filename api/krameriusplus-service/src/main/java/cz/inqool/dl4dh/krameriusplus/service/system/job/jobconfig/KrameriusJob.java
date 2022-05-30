package cz.inqool.dl4dh.krameriusplus.service.system.job.jobconfig;

import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.JobEventQueue;
import lombok.Getter;

import java.util.Set;

public enum KrameriusJob {
    ENRICHMENT_KRAMERIUS(JobEventQueue.ENRICHING_QUEUE.getQueueName()),
    ENRICHMENT_EXTERNAL(JobEventQueue.ENRICHING_QUEUE.getQueueName()),
    ENRICHMENT_NDK(JobEventQueue.ENRICHING_QUEUE.getQueueName()),
    ENRICHMENT_TEI(JobEventQueue.ENRICHING_QUEUE.getQueueName()),
    EXPORT(JobEventQueue.EXPORTING_QUEUE.getQueueName());

    @Getter
    private final String queueName;

    KrameriusJob(String queueName) {
        this.queueName = queueName;
    }

    public static Set<KrameriusJob> getEnrichingJobs() {
        return Set.of(ENRICHMENT_KRAMERIUS, ENRICHMENT_EXTERNAL, ENRICHMENT_NDK, ENRICHMENT_TEI);
    }

    public static Set<KrameriusJob> getExportingJobs() {
        return Set.of(EXPORT);
    }
}
