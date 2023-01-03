package cz.inqool.dl4dh.krameriusplus.core.jms;

import cz.inqool.dl4dh.krameriusplus.api.batch.job.KrameriusJobInstanceDto;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstance;

public interface JobEnqueueService {

    void enqueue(KrameriusJobInstance job);

    void enqueue(KrameriusJobInstanceDto jobDto);
}
