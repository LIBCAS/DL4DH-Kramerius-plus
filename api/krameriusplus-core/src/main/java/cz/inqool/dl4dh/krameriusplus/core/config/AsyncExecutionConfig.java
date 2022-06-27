package cz.inqool.dl4dh.krameriusplus.core.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author Norbert Bodnar
 */
@Configuration
@EnableAsync
public class AsyncExecutionConfig extends AsyncConfigurerSupport {

    @Override
    @Nullable
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }
}
