package cz.inqool.dl4dh.krameriusplus.core.domain.exception;

/**
 * Exception that occurs when running a Job
 *
 */
public class JobException extends GeneralException {

    public JobException(String message, ErrorCode errorCode) {
        super(message, errorCode, LogLevel.ERROR);
    }

    public enum ErrorCode implements LogCode {
        UNSUPPORTED_DIGITAL_OBJECT
    }
}
