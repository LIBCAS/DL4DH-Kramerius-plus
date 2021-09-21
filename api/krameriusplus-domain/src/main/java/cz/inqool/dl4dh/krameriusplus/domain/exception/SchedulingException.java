package cz.inqool.dl4dh.krameriusplus.domain.exception;

import lombok.Getter;

@Getter
public class SchedulingException extends GeneralException implements CodedException {

    private final ErrorCode errorCode;

    public SchedulingException(ErrorCode errorCode, Throwable throwable) {
        super(throwable);
        this.errorCode = errorCode;
    }

    public SchedulingException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public enum ErrorCode implements ExceptionCodeEnum {
        TASK_ALREADY_RUNNING,
        ALREADY_ENRICHED
    }
}
