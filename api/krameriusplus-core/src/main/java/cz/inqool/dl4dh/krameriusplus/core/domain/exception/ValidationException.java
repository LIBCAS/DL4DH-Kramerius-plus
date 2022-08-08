package cz.inqool.dl4dh.krameriusplus.core.domain.exception;

import lombok.Getter;

@Getter
public class ValidationException extends GeneralException {

    public ValidationException(String message, ErrorCode errorCode, Throwable throwable) {
        super(message, errorCode, LogLevel.ERROR, throwable);
    }

    public ValidationException(String message, ErrorCode errorCode) {
        super(message, errorCode, LogLevel.ERROR);
    }

    public enum ErrorCode implements LogCode {
        INVALID_EXPORT_TYPE,
        INVALID_PARAMETERS,
        DEPENDENCY_ERROR, ALREADY_EXISTS
    }
}
