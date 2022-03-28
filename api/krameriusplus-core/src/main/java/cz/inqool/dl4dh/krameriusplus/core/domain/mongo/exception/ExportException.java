package cz.inqool.dl4dh.krameriusplus.core.domain.mongo.exception;

import lombok.Getter;

@Getter
public class ExportException extends GeneralException {

    public ExportException(ErrorCode errorCode, Throwable throwable) {
        super(throwable.getMessage(), errorCode, LogLevel.ERROR, throwable);
    }

    public enum ErrorCode implements LogCode {
        TEI_MERGE_ERROR,
    }
}
