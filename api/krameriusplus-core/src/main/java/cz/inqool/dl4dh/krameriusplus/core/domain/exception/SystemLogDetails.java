package cz.inqool.dl4dh.krameriusplus.core.domain.exception;

import java.util.Map;

/**
 * Provides details of system warrnings & errors for logging purpose
 */
public interface SystemLogDetails {

    String getMessage();

    Enum<? extends LogCode> getErrorCode();

    LogLevel getErrorLevel();

    Map<String, String> getArgs();

    Throwable getCause();

    /**
     * Interface for custom enum log codes
     */
    interface LogCode {}

    /**
     * Specifies application logging level
     */
    enum LogLevel {
        INFO,
        WARNING,
        ERROR,
        FATAL_ERROR
    }

}
