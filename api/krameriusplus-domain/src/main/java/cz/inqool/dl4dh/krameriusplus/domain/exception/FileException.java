package cz.inqool.dl4dh.krameriusplus.domain.exception;

import lombok.Getter;

@Getter
public class FileException extends GeneralException implements CodedException {

    private final ErrorCode errorCode;

    public FileException(ErrorCode errorCode, Throwable throwable) {
        super(throwable);
        this.errorCode = errorCode;
    }

    public FileException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public enum ErrorCode implements ExceptionCodeEnum {
        FILE_NOT_INITIALIZED,
        FILE_ALREADY_OPENED,
        FAILED_TO_OPEN_FILE,
        FAILED_TO_CLOSE_FILE
    }
}
