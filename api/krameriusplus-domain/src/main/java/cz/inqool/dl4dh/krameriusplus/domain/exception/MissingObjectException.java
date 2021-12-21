package cz.inqool.dl4dh.krameriusplus.domain.exception;

import lombok.Getter;

@Getter
public class MissingObjectException extends GeneralException implements CodedException {

    private final ErrorCode errorCode;

    public MissingObjectException(ErrorCode errorCode, Throwable throwable) {
        super(throwable);
        this.errorCode = errorCode;
    }

    public MissingObjectException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public MissingObjectException(ErrorCode errorCode, String message, Throwable throwable) {
        super(message, throwable);
        this.errorCode = errorCode;
    }

    public enum ErrorCode implements ExceptionCodeEnum {
        PAGE_NOT_FOUND,
        PUBLICATION_NOT_FOUND,
        DIGITAL_OBJECT_NOT_FOUND
    }
}
