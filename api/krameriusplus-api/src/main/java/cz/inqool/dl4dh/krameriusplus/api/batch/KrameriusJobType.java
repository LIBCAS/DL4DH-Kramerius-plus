package cz.inqool.dl4dh.krameriusplus.api.batch;

import static cz.inqool.dl4dh.krameriusplus.api.batch.JobQueue.*;

public enum KrameriusJobType {
    CREATE_ENRICHMENT_REQUEST(DEFAULT_QUEUE),
    CREATE_EXPORT_REQUEST(DEFAULT_QUEUE),

    ENRICHMENT_EXTERNAL(ENRICHMENT_QUEUE),
    ENRICHMENT_NDK(ENRICHMENT_QUEUE),
    ENRICHMENT_TEI(ENRICHMENT_QUEUE),

    EXPORT_ALTO(EXPORT_QUEUE),
    EXPORT_TEXT(EXPORT_QUEUE),
    EXPORT_CSV(EXPORT_QUEUE),
    EXPORT_JSON(EXPORT_QUEUE),
    EXPORT_TEI(EXPORT_QUEUE),

    MERGE_JOB(DEFAULT_QUEUE)
    ;

    private final JobQueue queue;

    KrameriusJobType(JobQueue queue) {
        this.queue = queue;
    }
}
