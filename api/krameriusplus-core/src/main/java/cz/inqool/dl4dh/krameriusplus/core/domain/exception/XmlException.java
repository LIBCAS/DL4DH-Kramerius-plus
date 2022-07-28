package cz.inqool.dl4dh.krameriusplus.core.domain.exception;

/**
 * Exception for .xml related problems
 */
public class XmlException extends GeneralException{
    public XmlException(String message, ErrorCode errorCode) {
        super(message, errorCode, LogLevel.ERROR);
    }

    public enum ErrorCode implements LogCode {
        MISSING_TAG
    }
}
