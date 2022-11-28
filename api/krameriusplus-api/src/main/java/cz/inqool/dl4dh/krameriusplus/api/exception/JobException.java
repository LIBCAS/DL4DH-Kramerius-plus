package cz.inqool.dl4dh.krameriusplus.api.exception;

/**
 * Exception that occurs when running or creating a Job
 *
 */
public class JobException extends GeneralException {

    public JobException(String message, ErrorCode errorCode, Throwable cause) {
        super(message, errorCode, LogLevel.ERROR, cause);
    }

    public enum ErrorCode implements LogCode {
        INVALID_JOB_PARAMETERS,
        NOT_RESTARTABLE,
        NOT_RUNNING,
        IS_RUNNING,
        IS_COMPLETED,
        UNKNOWN_STATUS
    }
}
