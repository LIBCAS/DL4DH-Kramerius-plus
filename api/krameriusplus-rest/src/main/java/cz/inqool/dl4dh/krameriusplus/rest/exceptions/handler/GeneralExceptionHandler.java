package cz.inqool.dl4dh.krameriusplus.rest.exceptions.handler;

import cz.inqool.dl4dh.krameriusplus.api.exception.GeneralException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class GeneralExceptionHandler implements ExceptionHandler<GeneralException> {

    @Override
    public Class<GeneralException> getType() {
        return GeneralException.class;
    }

    @Override
    public String getMessage(@NonNull GeneralException throwable) {
        return throwable.getMessage() == null ? throwable.toString() : throwable.getMessage();
    }
}
