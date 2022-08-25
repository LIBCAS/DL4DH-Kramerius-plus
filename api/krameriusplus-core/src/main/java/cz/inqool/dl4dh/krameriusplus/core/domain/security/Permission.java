package cz.inqool.dl4dh.krameriusplus.core.domain.security;

public interface Permission {

    String ENRICHMENT_EXECUTE = "ENRICHMENT_EXECUTE";

    String EXPORT_EXECUTE = "EXPORT_EXECUTE";
    String EXPORT_READ = "EXPORT_READ";
    String EXPORT_LIST = "EXPORT_LIST";
    String EXPORT_LIST_OWN = "EXPORT_LIST_OWN";
    String EXPORT_DOWNLOAD = "EXPORT_DOWNLOAD";

    String JOB_READ = "JOB_READ";
    String JOB_LIST = "JOB_LIST";
    String JOB_LIST_OWN = "JOB_LIST_OWN";
    String JOB_STOP = "JOB_STOP";
    String JOB_RESTART = "JOB_RESTART";

    String PUBLICATION_READ = "PUBLICATION_READ";
    String PUBLICATION_LIST = "PUBLICATION_LIST";
    String PUBLICATION_PUBLISH = "PUBLICATION_PUBLISH";

    String USER_CREATE = "USER_CREATE";
    String USER_UPDATE = "USER_UPDATE";
    String USER_LIST = "USER_LIST";
    String USER_READ = "USER_READ";
    String USER_DELETE = "USER_DELETE";
}
