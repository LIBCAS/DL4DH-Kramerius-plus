package cz.inqool.dl4dh.krameriusplus.domain.exception;

import lombok.Getter;

@Getter
public class ExportException extends GeneralException implements CodedException {

    private final ErrorCode errorCode;

    public ExportException(ErrorCode errorCode, Throwable throwable) {
        super(throwable);
        this.errorCode = errorCode;
    }

    public ExportException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public enum ErrorCode implements ExceptionCodeEnum {
        TEI_MERGE_ERROR,
    }
}
