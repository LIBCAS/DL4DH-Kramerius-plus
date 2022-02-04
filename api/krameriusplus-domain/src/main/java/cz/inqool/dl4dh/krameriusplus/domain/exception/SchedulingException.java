package cz.inqool.dl4dh.krameriusplus.domain.exception;

import lombok.Getter;

@Getter
public class SchedulingException extends GeneralException {

    public SchedulingException(ErrorCode errorCode, String message) {
        super(message, errorCode, LogLevel.ERROR);
    }

    public enum ErrorCode implements LogCode {
        TASK_ALREADY_RUNNING,
        ALREADY_ENRICHED
    }
}
