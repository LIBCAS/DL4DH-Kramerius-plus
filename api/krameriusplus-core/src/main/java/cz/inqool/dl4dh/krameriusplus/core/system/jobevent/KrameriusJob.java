package cz.inqool.dl4dh.krameriusplus.core.system.jobevent;

import lombok.Getter;

import java.util.Collections;
import java.util.Set;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventQueue.ENRICHMENT_QUEUE;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventQueue.EXPORT_QUEUE;

@Getter
public enum KrameriusJob {
    ENRICHMENT_KRAMERIUS(ENRICHMENT_QUEUE.getQueueName(), Collections.emptySet()),
    ENRICHMENT_EXTERNAL(ENRICHMENT_QUEUE.getQueueName(), Set.of(ENRICHMENT_KRAMERIUS)),
    ENRICHMENT_NDK(ENRICHMENT_QUEUE.getQueueName(), Set.of(ENRICHMENT_KRAMERIUS)),
    ENRICHMENT_TEI(ENRICHMENT_QUEUE.getQueueName(), Set.of(ENRICHMENT_EXTERNAL)),

    EXPORT_JSON(EXPORT_QUEUE.getQueueName(), Set.of(ENRICHMENT_EXTERNAL)),
    EXPORT_CSV(EXPORT_QUEUE.getQueueName(), Set.of(ENRICHMENT_EXTERNAL)),
    EXPORT_TEI(EXPORT_QUEUE.getQueueName(), Set.of(ENRICHMENT_TEI)),
    EXPORT_ALTO(EXPORT_QUEUE.getQueueName(), Collections.emptySet()),
    EXPORT_TEXT(EXPORT_QUEUE.getQueueName(), Collections.emptySet());

    private final String queueName;

    /**
     * Collection of KrameriusJobs, on which this job is dependent. To start a job of this type,
     * there must exist at least one job of each type defined in this collection for the same publication.
     */
    private final Set<KrameriusJob> dependentOn;

    KrameriusJob(String queueName, Set<KrameriusJob> dependentOn) {
        this.queueName = queueName;
        this.dependentOn = dependentOn;
    }

    public static Set<KrameriusJob> getEnrichingJobs() {
        return Set.of(ENRICHMENT_KRAMERIUS, ENRICHMENT_EXTERNAL, ENRICHMENT_NDK, ENRICHMENT_TEI);
    }

    public static Set<KrameriusJob> getExportingJobs() {
        return Set.of(EXPORT_JSON, EXPORT_CSV, EXPORT_TEI, EXPORT_ALTO, EXPORT_TEXT);
    }
}
