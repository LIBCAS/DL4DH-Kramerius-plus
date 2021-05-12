package cz.inqool.dl4dh.krameriusplus.domain.exception;

import lombok.Getter;

/**
 * @author Norbert Bodnar
 */
@Getter
public class UDPipeException extends GeneralException implements CodedException {

    private final ErrorCode errorCode;

    public UDPipeException(ErrorCode errorCode, Throwable throwable) {
        super(throwable);
        this.errorCode = errorCode;
    }

    public UDPipeException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public enum ErrorCode implements ExceptionCodeEnum {
        EXTERNAL_SERVICE_ERROR
    }
}
