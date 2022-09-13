package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig;

/**
 * Helper class with defined constants, which can be used
 * in jobs to retrieve values from spring JobParameters
 */
public class JobParameterKey {
    public static final String PUBLICATION_ID = "publicationId";
    public static final String PARAMS = "params";
    public static final String TEI_EXPORT_PARAMS = "teiExportParams";
    public static final String JOB_EVENT_ID = "jobEventId";
    public static final String TIMESTAMP = "timestamp";
    public static final String KRAMERIUS_JOB = "krameriusJob";
    public static final String OVERRIDE = "override";
    public static final String DELIMITER = "delimiter";
    public static final String MISSING_ALTO_STRATEGY = "missingAltoStrategy";
}
