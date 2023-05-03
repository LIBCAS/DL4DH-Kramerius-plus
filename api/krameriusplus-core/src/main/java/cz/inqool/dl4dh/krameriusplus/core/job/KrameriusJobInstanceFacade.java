package cz.inqool.dl4dh.krameriusplus.core.job;

import cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus;
import cz.inqool.dl4dh.krameriusplus.api.batch.job.KrameriusJobInstanceDto;
import cz.inqool.dl4dh.krameriusplus.api.exception.JobException;
import cz.inqool.dl4dh.krameriusplus.api.job.JobFacade;
import cz.inqool.dl4dh.krameriusplus.core.jms.JmsProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
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
    @Transactional
    public void stopJob(String id) {
        if (!service.stopJob(id)) {
            throw new JobException(id, "Signal to stop Job couldn't be sent", JobException.ErrorCode.UNKNOWN_STATUS);
        }
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
