package cz.inqool.dl4dh.krameriusplus.cfg.exceptions.handler;

import org.springframework.lang.NonNull;

/**
 * Default exception handler, returning the exception message. Has the lowest priority.
 */
public class DefaultExceptionHandler implements ExceptionHandler<Throwable> {

    @Override
    public Class<Throwable> getType() {
        return Throwable.class;
    }

    @Override
    public boolean canHandle(@NonNull Class<? extends Throwable> throwable) {
        return true;
    }

    /**
     * Returns "1" to always be last in order.
     */
    @Override
    public int compareTo(@NonNull ExceptionHandler<?> o) {
        return 1;
    }
}
