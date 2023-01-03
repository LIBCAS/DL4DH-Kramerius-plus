package cz.inqool.dl4dh.krameriusplus.core.job;

import cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus;
import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.api.batch.job.KrameriusJobInstanceDto;
import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.job.config.JobParametersMapWrapper;
import lombok.Getter;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

@Service
public class KrameriusJobInstanceService {

    @Getter
    private KrameriusJobInstanceStore store;

    private KrameriusJobInstanceMapper mapper;

    private JobExplorer jobExplorer;

    @Transactional(readOnly = true)
    public KrameriusJobInstance findEntity(String jobInstanceId) {
        return store.findById(jobInstanceId)
                .orElseThrow(() -> new MissingObjectException(KrameriusJobInstance.class, jobInstanceId));
    }

    @Transactional(readOnly = true)
    public KrameriusJobInstanceDto find(String id) {
        return mapper.toDto(findEntity(id));
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
        JobExecution lastExecution = getLastExecution(instance.getJobInstanceId());
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

    private JobExecution getLastExecution(Long jobInstanceId) {
        JobInstance jobInstance = jobExplorer.getJobInstance(jobInstanceId);
        if (jobInstance == null) {
            return null;
        }

        JobExecution lastExecution = jobExplorer.getLastJobExecution(jobInstance);
        notNull(lastExecution, () -> new MissingObjectException(JobExecution.class, String.valueOf(jobInstanceId)));

        return lastExecution;
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
    public void setJobExplorer(JobExplorer jobExplorer) {
        this.jobExplorer = jobExplorer;
    }
}
