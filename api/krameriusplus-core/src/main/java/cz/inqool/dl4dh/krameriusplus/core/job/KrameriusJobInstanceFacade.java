package cz.inqool.dl4dh.krameriusplus.core.job;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus;
import cz.inqool.dl4dh.krameriusplus.api.batch.job.KrameriusJobInstanceDto;
import cz.inqool.dl4dh.krameriusplus.api.exception.JobException;
import cz.inqool.dl4dh.krameriusplus.api.job.JobEventFilter;
import cz.inqool.dl4dh.krameriusplus.api.job.JobFacade;
import cz.inqool.dl4dh.krameriusplus.core.jms.JmsProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus.*;

@Component
public class KrameriusJobInstanceFacade implements JobFacade {

    private KrameriusJobInstanceService service;

    private JmsProducer jmsProducer;

    @Override
    public KrameriusJobInstanceDto findJob(String id) {
        return service.find(id);
    }

    @Override
    public void restartJob(String id) {
        KrameriusJobInstanceDto jobInstance = service.find(id);

        List<ExecutionStatus> restartableStatuses = List.of(FAILED_FATALLY, FAILED, STOPPED);

        if (restartableStatuses.contains(jobInstance.getExecutionStatus())) {
            jmsProducer.enqueue(jobInstance);
        } else {
            throw new JobException(id, "Cannot restart job with executionStatus: " + jobInstance.getExecutionStatus(),
                    JobException.ErrorCode.NOT_RESTARTABLE);
        }
    }

    @Override
    public void stopJob(String id) {
        service.stopJob(id);
    }

    @Autowired
    public void setService(KrameriusJobInstanceService service) {
        this.service = service;
    }

    @Autowired
    public void setJmsProducer(JmsProducer jmsProducer) {
        this.jmsProducer = jmsProducer;
    }
}
