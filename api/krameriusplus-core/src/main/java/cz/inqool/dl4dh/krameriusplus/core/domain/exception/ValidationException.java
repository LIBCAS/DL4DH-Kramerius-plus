package cz.inqool.dl4dh.krameriusplus.core.domain.exception;

import lombok.Getter;

@Getter
public class ValidationException extends GeneralException {

    public ValidationException(String message, ErrorCode errorCode, Throwable throwable) {
        super(message, errorCode, LogLevel.ERROR, throwable);
    }

    public enum ErrorCode implements LogCode {
        INVALID_EXPORT_TYPE
    }
}
