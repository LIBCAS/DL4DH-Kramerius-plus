package cz.inqool.dl4dh.krameriusplus.core.job.listener;

import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstance;

public interface KrameriusJobListener {

    void beforeJob(KrameriusJobInstance jobInstance);

    void afterJob(KrameriusJobInstance jobInstance);

    boolean supports(KrameriusJobInstance jobInstance);
}
