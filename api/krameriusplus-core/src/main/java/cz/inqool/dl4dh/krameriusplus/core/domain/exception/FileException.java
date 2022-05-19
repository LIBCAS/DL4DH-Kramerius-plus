package cz.inqool.dl4dh.krameriusplus.core.domain.exception;

import lombok.Getter;

@Getter
public class FileException extends GeneralException {

    public FileException(ErrorCode errorCode, Throwable throwable) {
        super(throwable.getMessage(), errorCode, LogLevel.ERROR, throwable);
    }

    public FileException(ErrorCode errorCode, String message) {
        super(message, errorCode, LogLevel.ERROR);
    }

    public enum ErrorCode implements LogCode {
        FILE_NOT_INITIALIZED,
        FILE_ALREADY_OPENED,
        FAILED_TO_OPEN_FILE,
        FAILED_TO_CLOSE_FILE
    }
}
