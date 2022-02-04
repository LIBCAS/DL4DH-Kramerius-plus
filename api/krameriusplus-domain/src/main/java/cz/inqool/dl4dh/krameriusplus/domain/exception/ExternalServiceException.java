package cz.inqool.dl4dh.krameriusplus.domain.exception;

import lombok.Getter;

@Getter
public class ExternalServiceException extends GeneralException {

    public ExternalServiceException(ErrorCode errorCode, Throwable throwable) {
        super(throwable.getMessage(), errorCode, LogLevel.ERROR, throwable);
    }

    public ExternalServiceException(String message, ErrorCode errorCode, LogLevel logLevel) {
        super(message, errorCode, logLevel);
    }

    public enum ErrorCode implements LogCode {
        UD_PIPE_ERROR,
        NAME_TAG_ERROR,
        TEI_ERROR
    }
}
