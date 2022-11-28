package cz.inqool.dl4dh.krameriusplus.api.exception;

/**
 * Exception for .xml related problems
 */
public class XmlException extends GeneralException{
    public XmlException(String message, ErrorCode errorCode) {
        super(message, errorCode, LogLevel.ERROR);
    }

    public enum ErrorCode implements LogCode {
        MISSING_TAG,
        INVALID_NUMBER_OF_ELEMENTS
    }
}
