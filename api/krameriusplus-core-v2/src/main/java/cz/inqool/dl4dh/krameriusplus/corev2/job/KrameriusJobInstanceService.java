package cz.inqool.dl4dh.krameriusplus.corev2.job;

import cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus;
import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.corev2.job.config.JobParametersMapWrapper;
import lombok.Getter;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KrameriusJobInstanceService {

    @Getter
    private KrameriusJobInstanceStore store;

    private KrameriusJobInstanceMapper mapper;

    public KrameriusJobInstance find(String jobInstanceId) {
        return store.findById(jobInstanceId)
                .orElseThrow(() -> new MissingObjectException(KrameriusJobInstance.class, jobInstanceId));
    }

    /**
     * Creates a new KrameriusJobInstance. Actual Spring Batch JobInstance is created when the job
     * is actually launched
     */
    @Transactional
    public KrameriusJobInstance createJobInstance(KrameriusJobType jobType, JobParametersMapWrapper jobParametersMap) {
        KrameriusJobInstance krameriusJobInstance = new KrameriusJobInstance();
        krameriusJobInstance.setExecutionStatus(ExecutionStatus.CREATED);
        krameriusJobInstance.setJobType(jobType);

        JobParameters jobParameters = mapper.toJobParameters(krameriusJobInstance, jobParametersMap.getJobParametersMap());
        krameriusJobInstance.setJobParameters(jobParameters);

        return store.save(krameriusJobInstance);
    }

    @Transactional
    public void updateStatus(KrameriusJobInstance instance) {
        // 1. get last JobExecution from mapper
        // 2. set status and update KrameriusJobInstance
        JobExecution lastExecution = mapper.toLastExecution(instance.getJobInstanceId());
        if (lastExecution == null) {
            return;
        }

        instance.setExecutionStatus(ExecutionStatus.valueOf(lastExecution.getStatus().toString()));

        store.save(instance);
    }

    @Transactional
    public void assignInstance(KrameriusJobInstance krameriusJobInstance, JobInstance jobInstance) {
        krameriusJobInstance.setJobInstanceId(jobInstance.getInstanceId());
        krameriusJobInstance.setExecutionStatus(ExecutionStatus.CREATED);

        store.save(krameriusJobInstance);
    }

    @Autowired
    public void setStore(KrameriusJobInstanceStore store) {
        this.store = store;
    }

    @Autowired
    public void setMapper(KrameriusJobInstanceMapper mapper) {
        this.mapper = mapper;
    }
}
