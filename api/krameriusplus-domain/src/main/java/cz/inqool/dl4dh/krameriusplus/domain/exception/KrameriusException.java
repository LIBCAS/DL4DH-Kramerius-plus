package cz.inqool.dl4dh.krameriusplus.domain.exception;

import lombok.Getter;

/**
 * @author Norbert Bodnar
 */
@Getter
public class KrameriusException extends GeneralException implements CodedException {

    private final ErrorCode errorCode;

    public KrameriusException(ErrorCode errorCode, Throwable throwable) {
        super(throwable);
        this.errorCode = errorCode;
    }

    public KrameriusException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public enum ErrorCode implements ExceptionCodeEnum {
        NO_CHILDREN,
        INVALID_MODEL,
        EXTERNAL_API_ERROR
    }
}
