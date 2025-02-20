package cz.inqool.dl4dh.krameriusplus.api.job;

public interface JobListenerFacade {

    void refresh();

    void start();

    void stop();

    String status();
}
