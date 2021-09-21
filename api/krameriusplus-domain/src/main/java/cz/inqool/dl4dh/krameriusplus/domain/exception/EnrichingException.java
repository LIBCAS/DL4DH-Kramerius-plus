package cz.inqool.dl4dh.krameriusplus.domain.exception;

import lombok.Getter;

@Getter
public class EnrichingException extends GeneralException implements CodedException {

    private final ErrorCode errorCode;

    public EnrichingException(ErrorCode errorCode, Throwable throwable) {
        super(throwable);
        this.errorCode = errorCode;
    }

    public EnrichingException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public enum ErrorCode implements ExceptionCodeEnum {
        KRAMERIUS_ERROR,
        TEI_ERROR,
        SERIALIZING_ERROR,
        TYPE_ERROR
    }
}
