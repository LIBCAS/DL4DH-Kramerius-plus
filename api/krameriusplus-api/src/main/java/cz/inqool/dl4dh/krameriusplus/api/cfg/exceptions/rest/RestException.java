package cz.inqool.dl4dh.krameriusplus.api.cfg.exceptions.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import cz.inqool.dl4dh.krameriusplus.api.cfg.exceptions.ExceptionDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * Class representing an exception raised during execution of a REST endpoint
 */
@Getter
@JsonInclude(Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonPropertyOrder({"status", "error", "path", "timestamp", "code", "exception", "message", "cause", "details"})
public class RestException extends ExceptionDTO {

    private final int status;
    private final String error;
    private final String path;


    public RestException(HttpServletRequest request, HttpStatus status, Throwable exception, String message) {
        super(exception, message);
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.path = request.getMethod() + ": " + request.getRequestURI();
    }
}
