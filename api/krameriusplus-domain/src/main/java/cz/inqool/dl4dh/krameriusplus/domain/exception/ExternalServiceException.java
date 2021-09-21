package cz.inqool.dl4dh.krameriusplus.domain.exception;

import lombok.Getter;

@Getter
public class ExternalServiceException extends GeneralException implements CodedException {

    private final ErrorCode errorCode;

    public ExternalServiceException(ErrorCode errorCode, Throwable throwable) {
        super(throwable);
        this.errorCode = errorCode;
    }

    public ExternalServiceException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public enum ErrorCode implements ExceptionCodeEnum {
        UD_PIPE_ERROR,
        NAME_TAG_ERROR
    }
}
