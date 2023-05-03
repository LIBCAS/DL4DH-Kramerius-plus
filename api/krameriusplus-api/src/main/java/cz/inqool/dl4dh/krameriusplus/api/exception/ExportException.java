package cz.inqool.dl4dh.krameriusplus.api.exception;

import lombok.Getter;

@Getter
public class ExportException extends GeneralException {

    public ExportException(String message, ErrorCode errorCode) {
        super(message, errorCode, LogLevel.ERROR);
    }

    public ExportException(ErrorCode errorCode, Throwable throwable) {
        super(throwable.getMessage(), errorCode, LogLevel.ERROR, throwable);
    }

    public enum ErrorCode implements LogCode {
        TEI_MERGE_ERROR,
        REQUEST_FINISHED,
        EXPORT_FILE_MISSING,
    }
}
