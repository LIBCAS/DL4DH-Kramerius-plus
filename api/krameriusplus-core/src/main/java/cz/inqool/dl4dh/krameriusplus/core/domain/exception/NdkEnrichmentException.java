package cz.inqool.dl4dh.krameriusplus.core.domain.exception;

public class NdkEnrichmentException extends GeneralException {

    public NdkEnrichmentException(String message, ErrorCode errorCode, Throwable throwable) {
        super(message, errorCode, LogLevel.ERROR, throwable);
    }

    public NdkEnrichmentException(String message, ErrorCode errorCode) {
        super(message, errorCode, LogLevel.ERROR);
    }

    public enum ErrorCode implements LogCode {
        NDK_PAGE_FILE_NOT_FOUND,
        NDK_PAGE_DIV_NOT_FOUND,
        NDK_PAGE_DIV_AMD_CHILD_NOT_FOUND,
        NDK_DIRECTORY_NOT_FOUND,
        NDK_DIRECTORY_NOT_CONFIGURED,
        MAIN_METS_ERROR,
        PAGE_METS_ERROR
    }
}
