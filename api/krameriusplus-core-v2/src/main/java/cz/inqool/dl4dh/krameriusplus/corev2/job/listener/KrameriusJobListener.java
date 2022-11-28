package cz.inqool.dl4dh.krameriusplus.corev2.job.listener;

import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;

public interface KrameriusJobListener {

    void afterJob(KrameriusJobInstance jobInstance);

    boolean supports(KrameriusJobInstance jobInstance);
}
