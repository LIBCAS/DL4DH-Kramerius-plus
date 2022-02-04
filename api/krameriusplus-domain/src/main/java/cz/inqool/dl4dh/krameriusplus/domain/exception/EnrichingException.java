package cz.inqool.dl4dh.krameriusplus.domain.exception;

import lombok.Getter;

@Getter
public class EnrichingException extends GeneralException {

    public EnrichingException(ErrorCode errorCode, Throwable throwable) {
        super(throwable.getMessage(), errorCode, LogLevel.ERROR, throwable);
    }

    public enum ErrorCode implements LogCode {
        KRAMERIUS_ERROR,
        TEI_ERROR,
        SERIALIZING_ERROR,
        TYPE_ERROR
    }
}
