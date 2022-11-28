package cz.inqool.dl4dh.krameriusplus.corev2.jms;

import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;

public interface JobEnqueueService {

    void enqueue(KrameriusJobInstance job);
}
