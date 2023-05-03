package cz.inqool.dl4dh.krameriusplus.api.exception;

import lombok.Getter;

/**
 * Exception that occurs when running or creating a Job
 *
 */
public class JobException extends GeneralException {

    @Getter
    private final String krameriusJobInstanceId;

    public JobException(String krameriusJobInstanceId, String message, ErrorCode errorCode) {
        super(message, errorCode, LogLevel.ERROR);
        this.krameriusJobInstanceId = krameriusJobInstanceId;
    }

    public JobException(String krameriusJobInstanceId, String message, ErrorCode errorCode, Throwable cause) {
        super(message, errorCode, LogLevel.ERROR, cause);
        this.krameriusJobInstanceId = krameriusJobInstanceId;
    }

    public enum ErrorCode implements LogCode {
        INVALID_JOB_PARAMETERS,
        NOT_RESTARTABLE,
        NOT_RUNNING,
        IS_RUNNING,
        IS_COMPLETED,
        UNKNOWN_STATUS,
        NO_EXECUTION,
        NOT_STARTABLE_STATUS,
        JOB_OPERATOR_ERROR
    }
}
