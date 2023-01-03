package cz.inqool.dl4dh.krameriusplus.api.exception;

public class TeiEnrichmentException extends GeneralException {

    public TeiEnrichmentException(String message, ErrorCode errorCode, Throwable throwable) {
        super(message, errorCode, LogLevel.ERROR, throwable);
    }

    public TeiEnrichmentException(String message, ErrorCode errorCode) {
        super(message, errorCode, LogLevel.ERROR);
    }

    public enum ErrorCode implements LogCode {
        MISSING_TEI_HEADER_FILE,
        MISSING_TEI_BODY_FILE,
    }
}
