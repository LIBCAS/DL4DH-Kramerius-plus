package cz.inqool.dl4dh.krameriusplus.core.domain.exception;

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

    public enum ErrorCode implements LogCode {
        NOT_FOUND,
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
