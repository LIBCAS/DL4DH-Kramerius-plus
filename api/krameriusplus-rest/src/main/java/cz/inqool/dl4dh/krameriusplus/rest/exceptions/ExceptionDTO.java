package cz.inqool.dl4dh.krameriusplus.rest.exceptions;

import cz.inqool.dl4dh.krameriusplus.api.exception.GeneralException;
import cz.inqool.dl4dh.krameriusplus.api.exception.SystemLogDetails;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.NonNull;

import java.time.Instant;

/**
 * Abstract class serving as a base for exception handling objects.
 */
@Getter
@ToString
public abstract class ExceptionDTO {

    protected String timestamp;
    protected Enum<? extends SystemLogDetails.LogCode> code;
    protected Class<?> exception;
    protected String message;
    protected String cause;

    protected ExceptionDTO(@NonNull Throwable exception) {
        this(exception, exception.getMessage());
    }

    protected ExceptionDTO(@NonNull Throwable exception, String message) {
        this.timestamp = Instant.now().toString();
        if (exception instanceof SystemLogDetails) {
            this.code = ((SystemLogDetails) exception).getErrorCode();
        }
        this.message = message;
        this.exception = exception.getClass();


        Throwable cause = getLastCause(exception);
        if (cause != exception) {
            if (cause instanceof GeneralException) {
                this.cause = cause.toString();
            } else {
                this.cause = cause.getClass().getName() + ": " + cause.getMessage();
            }
        }
    }


    private static Throwable getLastCause(Throwable exception) {
        Throwable cause = exception;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }

        return cause;
    }
}
