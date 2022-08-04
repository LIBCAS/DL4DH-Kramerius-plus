package cz.inqool.dl4dh.krameriusplus.core.domain.exception;

/**
 * Exception that occurs when running or creating a Job
 *
 */
public class JobException extends GeneralException {

    public JobException(String message, ErrorCode errorCode) {
        super(message, errorCode, LogLevel.ERROR);
    }

    public enum ErrorCode implements LogCode {
        INVALID_JOB_PARAMETERS,
        NOT_RESTARTABLE,
        ALREADY_RUNNING,
        UNKNOWN_STATUS,
        ALREADY_COMPLETE
    }
}
