package cz.inqool.dl4dh.krameriusplus.api.exception;

import java.util.Map;

/**
 * Class representing general exception which is extended by every custom exception in project
 */
public abstract class GeneralException extends RuntimeException implements SystemLogDetails {

    private final Enum<? extends LogCode> errorCode;

    private final LogLevel errorLevel;

    private Map<String, String> args;

    private Throwable cause;

    public GeneralException(Enum<? extends LogCode> errorCode, LogLevel errorLevel) {
        super(errorLevel + "-" + errorCode);
        this.errorCode = errorCode;
        this.errorLevel = errorLevel;
    }

    public GeneralException(String message, Enum<? extends LogCode> errorCode, LogLevel errorLevel) {
        super(message);
        this.errorCode = errorCode;
        this.errorLevel = errorLevel;
    }

    public GeneralException(String message, Enum<? extends LogCode> errorCode, LogLevel errorLevel, Map<String, String> args) {
        super(message);
        this.errorCode = errorCode;
        this.errorLevel = errorLevel;
        this.args = args;
    }

    public GeneralException(String message, Enum<? extends LogCode> errorCode, LogLevel errorLevel, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorLevel = errorLevel;
        this.cause = cause;
    }

    public GeneralException(
            String message, Enum<? extends LogCode> errorCode, LogLevel errorLevel, Map<String, String> args, Throwable cause
    ) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorLevel = errorLevel;
        this.args = args;
        this.cause = cause;
    }

    @Override
    public String getMessage() {
        return this.toString();
    }

    @Override
    public Enum<? extends LogCode> getErrorCode() {
        return errorCode;
    }

    @Override
    public LogLevel getErrorLevel() {
        return errorLevel;
    }

    @Override
    public Map<String, String> getArgs() {
        return args;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    @Override
    public String toString() {
        return super.getMessage();
    }

    public enum GeneralErrorCode implements LogCode {
        NOT_SPECIFIED
    }
}
