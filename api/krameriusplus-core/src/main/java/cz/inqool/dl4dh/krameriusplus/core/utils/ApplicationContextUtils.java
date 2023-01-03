package cz.inqool.dl4dh.krameriusplus.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class ApplicationContextUtils implements ApplicationContextAware {

    private static ApplicationContext ctx;


    @Override
    public void setApplicationContext(@NonNull ApplicationContext context) throws BeansException {
        ctx = context;
    }

    public static ApplicationContext getApplicationContext() {
        return ctx;
    }
}
