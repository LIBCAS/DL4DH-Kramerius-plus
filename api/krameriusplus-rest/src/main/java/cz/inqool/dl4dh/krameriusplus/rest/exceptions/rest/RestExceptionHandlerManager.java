package cz.inqool.dl4dh.krameriusplus.rest.exceptions.rest;

import cz.inqool.dl4dh.krameriusplus.rest.exceptions.handler.DefaultExceptionHandler;
import cz.inqool.dl4dh.krameriusplus.rest.exceptions.handler.ExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Compoment class for custom error message extraction (to provide more user-friendly description)
 */
@Component
@ApplicationScope
public class RestExceptionHandlerManager {

    private static final ExceptionHandler<Throwable> DEFAULT_PARSER = new DefaultExceptionHandler();
    private final Map<Class<? extends Throwable>, ExceptionHandler<?>> parserCache = new HashMap<>();

    private Set<ExceptionHandler<?>> parsers;


    public <T extends Throwable> ResponseEntity<RestException> handleException(@NonNull HttpServletRequest request, @NonNull T throwable, @NonNull HttpStatus status) {
        return getHandler(throwable).handle(request, throwable, status);
    }

    private <T extends Throwable> ExceptionHandler<T> getHandler(@NonNull T throwable) {
        Class<? extends Throwable> throwableClass = throwable.getClass();
        if (!parserCache.containsKey(throwableClass)) {
            ExceptionHandler<?> exceptionhandler = parsers.stream()
                    .filter(parser -> parser.canHandle(throwableClass))
                    .sorted()
                    .findFirst().orElse(DEFAULT_PARSER);
            parserCache.put(throwableClass, exceptionhandler);
        }

        //noinspection unchecked
        return (ExceptionHandler<T>) parserCache.get(throwableClass);
    }


    @Autowired
    public void setExceptionParsers(Set<ExceptionHandler<?>> parsers) {
        this.parsers = parsers;
    }
}
