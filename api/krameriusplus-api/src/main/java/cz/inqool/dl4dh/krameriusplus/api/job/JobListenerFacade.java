package cz.inqool.dl4dh.krameriusplus.api.job;

import java.util.Map;

public interface JobListenerFacade {

    void refresh();

    void start();

    void stop();

    Map<String, Boolean> status();
}
