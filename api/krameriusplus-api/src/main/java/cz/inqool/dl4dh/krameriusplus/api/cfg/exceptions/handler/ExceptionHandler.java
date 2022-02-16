package cz.inqool.dl4dh.krameriusplus.api.cfg.exceptions.handler;

import cz.inqool.dl4dh.krameriusplus.api.cfg.exceptions.HttpHeadersBuilder;
import cz.inqool.dl4dh.krameriusplus.api.cfg.exceptions.rest.RestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface for all custom exception message parsers.
 *
 * @param <T> type of exception handled by the parser
 */
public interface ExceptionHandler<T extends Throwable> extends Comparable<ExceptionHandler<?>> {

    Logger log = LoggerFactory.getLogger(ExceptionHandler.class);

    /**
     * Response headers to be included in response
     */
    HttpHeaders headers = new HttpHeadersBuilder()
            .contentType(MediaType.APPLICATION_JSON)
            .build();


    /**
     * Returns the type of generic class (to be able to find most suitable message parser)
     */
    Class<T> getType();

    /**
     * Returns {@code true} if this parser is able to handle exceptions of given type. On default, all subclasses of
     * parser's {@code type} can be handled.
     *
     * @param throwable exception type class
     * @return {@code true} if this parser is able to handle exceptions of given type, {@code false} otherwise
     */
    default boolean canHandle(@NonNull Class<? extends Throwable> throwable) {
        return getType().isAssignableFrom(throwable);
    }

    /**
     * Returns custom message from given {@code throwable}
     */
    default String getMessage(@NonNull T throwable) {
        return throwable.getMessage();
    }

    /**
     * Specifies that parsed exception should be logged
     */
    default boolean logException() {
        return true;
    }

    /**
     * Performs actual exception logging
     */
    default void logException(@NonNull T exception, @NonNull HttpServletRequest request, @NonNull RestException restException) {
        log.error("\n" + restException.toString(), exception);
    }

    /**
     * Handles an exception
     *
     * @param request which caused the exception
     * @param e       exception thrown during request execution
     * @param status  HTTP status adviced to be used in response
     * @return response entity
     */
    default ResponseEntity<RestException> handle(@NonNull HttpServletRequest request, @NonNull T e, @NonNull HttpStatus status) {
        RestException error = new RestException(request, status, e, getMessage(e));
        if (logException()) {
            logException(e, request, error);
        }
        return new ResponseEntity<>(error, headers, status);
    }

    /**
     * Compares two parsers. The comparison is based on class hierarchy - the superclass is always "greater" than its
     * sub-class.
     */
    @Override
    default int compareTo(@NonNull ExceptionHandler<?> o) {
        Class<T> type = this.getType();
        Class<?> oType = o.getType();

        if (type.equals(oType)) {
            return 0;
        } else if (type.isAssignableFrom(oType)) {
            return 1;
        } else {
            return -1;
        }
    }
}
