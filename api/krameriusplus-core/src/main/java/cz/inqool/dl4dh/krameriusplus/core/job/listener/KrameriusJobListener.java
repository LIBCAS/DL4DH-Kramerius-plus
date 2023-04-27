package cz.inqool.dl4dh.krameriusplus.core.job.listener;

import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstance;

public interface KrameriusJobListener {

    void beforeJob(String jobInstanceId);

    void afterJob(String jobInstanceId);

    boolean supports(KrameriusJobInstance jobInstance);
}
