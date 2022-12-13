package cz.inqool.dl4dh.krameriusplus.corev2.job;

import cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus;
import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
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

    private static final String UNEXPECTED_EXCEPTION_CODE = "UNEXPECTED_ERROR_OCCURRED";

    private static final String STOP_EXCEPTION_CODE = "STEP_STOPPED";

    @Getter
    private KrameriusJobInstanceStore store;

    private KrameriusJobInstanceMapper mapper;

    public KrameriusJobInstance find(String jobInstanceId) {
        return store.find(jobInstanceId);
    }

    @Transactional
    public KrameriusJobInstance createJobInstance(KrameriusJobType jobType, JobParametersMapWrapper jobParametersMap) {
        // 1. Create JobInstance from JobType + JobParameters, using mapper::toJobParameters
        // 2. Create KrameriusJobInstance with default executionStatus CREATED
        KrameriusJobInstance krameriusJobInstance = new KrameriusJobInstance();
        krameriusJobInstance.setExecutionStatus(ExecutionStatus.CREATED);
        krameriusJobInstance.setJobType(jobType);
        JobParameters jobParameters = mapper.toJobParameters(krameriusJobInstance, jobParametersMap.getJobParametersMap());

        krameriusJobInstance.setJobParameters(jobParameters);

        return store.create(krameriusJobInstance);
    }

    @Transactional
    public void updateStatus(KrameriusJobInstance instance) {
        // 1. get last JobExecution from mapper
        // 2. set status and update KrameriusJobInstance
        JobExecution lastExecution = mapper.toLastExecution(instance.getJobInstanceId());
        instance.setExecutionStatus(ExecutionStatus.valueOf(lastExecution.getStatus().toString()));

        store.update(instance);
    }

    @Transactional
    public void assignInstance(KrameriusJobInstance krameriusJobInstance, JobInstance jobInstance) {
        krameriusJobInstance.setJobInstanceId(jobInstance.getInstanceId());
        krameriusJobInstance.setExecutionStatus(ExecutionStatus.CREATED);
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
