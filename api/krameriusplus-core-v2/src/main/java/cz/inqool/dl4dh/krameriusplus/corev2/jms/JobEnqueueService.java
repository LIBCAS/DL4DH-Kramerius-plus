package cz.inqool.dl4dh.krameriusplus.corev2.jms;

import cz.inqool.dl4dh.krameriusplus.api.batch.job.KrameriusJobInstanceDto;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;

public interface JobEnqueueService {

    void enqueue(KrameriusJobInstance job);

    void enqueue(KrameriusJobInstanceDto jobDto);
}
