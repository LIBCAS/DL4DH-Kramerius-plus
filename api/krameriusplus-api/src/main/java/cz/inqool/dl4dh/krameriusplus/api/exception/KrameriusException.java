package cz.inqool.dl4dh.krameriusplus.api.exception;

import lombok.Getter;

/**
 * @author Norbert Bodnar
 */
@Getter
public class KrameriusException extends GeneralException {

    public KrameriusException(ErrorCode errorCode, Throwable throwable) {
        super(throwable.getMessage(), errorCode, LogLevel.ERROR, throwable);
    }

    public KrameriusException(ErrorCode errorCode, String message) {
        super(message, errorCode, LogLevel.ERROR);
    }

    public KrameriusException(ErrorCode errorCode, Throwable throwable, String message) {
        super(message, errorCode, LogLevel.ERROR, throwable);
    }

    public enum ErrorCode implements LogCode {
        NOT_FOUND,
        UNAUTHORIZED,
        MISSING_CHILDREN,
        EXTERNAL_API_ERROR,
        NOT_RESPONDING,
        UNDEFINED,
        MISSING_STREAM,
        NO_PAGE_HAD_ALTO,
        INTERRUPTED,
        PARSING_ERROR
    }
}
