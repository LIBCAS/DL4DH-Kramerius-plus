package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common;

import org.springframework.batch.core.JobParameters;

/**
 * Helper class with defined constants, which can be used
 * in jobs to retrieve values from {@link JobParameters}
 */
public class JobParameterKey {
    public static final String PUBLICATION_ID = "publicationId";
    public static final String PUBLICATION_TITLE = "publicationTitle";
    public static final String EXPORT_FORMAT = "exportFormat";
    public static final String PARAMS = "params";
    public static final String JOB_EVENT_ID = "jobEventId";
    public static final String KRAMERIUS_JOB = "krameriusJob";
    public static final String OVERRIDE = "override";
    public static final String DIRECTORY = "directory";
    public static final String ZIPPED_FILE = "zippedFile";
    public static final String DELIMITER = "delimiter";
}
