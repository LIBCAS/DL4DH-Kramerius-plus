package cz.inqool.dl4dh.krameriusplus.corev2.job;

import org.springframework.batch.core.JobParameters;

public interface JobProducer {

    JobParameters constructJobParameters();


}
