package cz.inqool.dl4dh.krameriusplus.corev2.job;

import cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus;
import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.corev2.job.config.JobParametersMapWrapper;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KrameriusJobInstanceService {

    private static final String UNEXPECTED_EXCEPTION_CODE = "UNEXPECTED_ERROR_OCCURRED";

    private static final String STOP_EXCEPTION_CODE = "STEP_STOPPED";

    private KrameriusJobInstanceStore store;

    private KrameriusJobInstanceMapper mapper;

    private JobRepository jobRepository;

    public KrameriusJobInstance find(String jobInstanceId) {
        return store.find(jobInstanceId);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public KrameriusJobInstance createJob(KrameriusJobType jobType, JobParametersMapWrapper jobParametersMap) {
        // 1. Create JobInstance from JobType + JobParameters, using mapper::toJobParameters
        // 2. Create KrameriusJobInstance with default executionStatus CREATED
        KrameriusJobInstance krameriusJobInstance = new KrameriusJobInstance();
        krameriusJobInstance.setExecutionStatus(ExecutionStatus.CREATED);
        krameriusJobInstance.setJobType(jobType);
        JobParameters jobParameters = mapper.toJobParameters(krameriusJobInstance, jobParametersMap.getJobParametersMap());

        krameriusJobInstance.setJobParameters(jobParameters);

        JobInstance jobInstance = jobRepository.createJobInstance(jobType.getName(), jobParameters);

        krameriusJobInstance.setJobInstanceId(jobInstance.getInstanceId());

        return store.create(krameriusJobInstance);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void updateStatus(KrameriusJobInstance instance) {
        // 1. get last JobExecution from mapper
        // 2. set status and update KrameriusJobInstance
        JobExecution lastExecution = mapper.toLastExecution(instance.getJobInstanceId());
        instance.setExecutionStatus(ExecutionStatus.valueOf(lastExecution.getStatus().toString()));

        store.update(instance);
    }

    @Autowired
    public void setStore(KrameriusJobInstanceStore store) {
        this.store = store;
    }

    @Autowired
    public void setMapper(KrameriusJobInstanceMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setJobRepository(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }
}
