package cz.inqool.dl4dh.krameriusplus.core.domain.mongo.exception;

import lombok.Getter;

@Getter
public class EnrichingException extends GeneralException {

    public EnrichingException(ErrorCode errorCode, Throwable throwable) {
        super(throwable.getMessage(), errorCode, LogLevel.ERROR, throwable);
    }

    public EnrichingException(String message, ErrorCode errorCode, Throwable throwable) {
        super(message, errorCode, LogLevel.ERROR, throwable);
    }

    public EnrichingException(String message, ErrorCode errorCode, LogLevel errorLevel) {
        super(message, errorCode, errorLevel);
    }

    public enum ErrorCode implements LogCode {
        KRAMERIUS_ERROR,
        TEI_ERROR,
        SERIALIZING_ERROR,
        TYPE_ERROR,
        INVALID_OBJECT_TYPE
    }
}
