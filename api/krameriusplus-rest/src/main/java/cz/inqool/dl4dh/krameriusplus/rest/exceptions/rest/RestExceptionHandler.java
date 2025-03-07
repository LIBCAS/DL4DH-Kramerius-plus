package cz.inqool.dl4dh.krameriusplus.rest.exceptions.rest;

import cz.inqool.dl4dh.krameriusplus.api.exception.JobException;
import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.api.exception.SchedulingException;
import cz.inqool.dl4dh.krameriusplus.api.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Class handling exceptions raised during REST endpoint executions. Provides unified and more detailed information
 * about exceptions.
 */
@ControllerAdvice
public class RestExceptionHandler {

    private RestExceptionHandlerManager restExceptionHandlerManager;

    @ExceptionHandler({
            SchedulingException.class,
            ValidationException.class,
            javax.validation.ValidationException.class,
            JobException.class,
            MethodArgumentNotValidException.class,
            BindException.class
    })
    public ResponseEntity<RestException> badRequest(HttpServletRequest request, Exception e) {
        return defaultExceptionHandling(request, e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            BadCredentialsException.class,
            UsernameNotFoundException.class
    })
    public ResponseEntity<RestException> unauthorized(HttpServletRequest request, Exception e) {
        return defaultExceptionHandling(request, e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({
            MissingObjectException.class
    })
    public ResponseEntity<RestException> notFound(HttpServletRequest request, Exception e) {
        return defaultExceptionHandling(request, e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<RestException> exception(HttpServletRequest request, Exception e) {
        return defaultExceptionHandling(request, e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<RestException> defaultExceptionHandling(@NonNull HttpServletRequest request, @NonNull Exception e, @NonNull HttpStatus status) {
        return restExceptionHandlerManager.handleException(request, e, status);
    }


    @Autowired
    public void setRestExceptionHandlerManager(RestExceptionHandlerManager parser) {
        this.restExceptionHandlerManager = parser;
    }
}
