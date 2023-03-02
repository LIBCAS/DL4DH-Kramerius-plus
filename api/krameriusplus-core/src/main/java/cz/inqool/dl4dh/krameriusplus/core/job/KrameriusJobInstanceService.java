package cz.inqool.dl4dh.krameriusplus.core.job;

import cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus;
import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.api.batch.LaunchStatus;
import cz.inqool.dl4dh.krameriusplus.api.batch.job.KrameriusJobInstanceDto;
import cz.inqool.dl4dh.krameriusplus.api.exception.JobException;
import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.job.config.JobParametersMapWrapper;
import lombok.Getter;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
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

    private JobOperator jobOperator;

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
    public void saveLaunchError(String instanceId, String message) {
        KrameriusJobInstance instance = findEntity(instanceId);

        LastLaunch lastLaunch = new LastLaunch();
        lastLaunch.setLaunchStatus(LaunchStatus.FAILED);
        lastLaunch.setMessage(message);

        instance.setLastLaunch(lastLaunch);

        store.save(instance);
    }

    @Transactional
    public void updateStatus(String instanceId, ExecutionStatus newStatus) {
        KrameriusJobInstance instance = findEntity(instanceId);

        instance.setExecutionStatus(newStatus);

        store.save(instance);
    }

    @Transactional
    public void updateStatus(String instanceId) {
        // 1. get last JobExecution from mapper
        // 2. set status and update KrameriusJobInstance

        // important to fetch up-to-date data
        KrameriusJobInstance instance = findEntity(instanceId);

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

    public void stopJob(String id) {
        KrameriusJobInstance krameriusJobInstance = store.findById(id).orElseThrow(() -> new MissingObjectException(KrameriusJobInstance.class, id));
        try {
            jobOperator.stop(getLastExecution(krameriusJobInstance.getJobInstanceId()).getId());
        } catch (JobExecutionNotRunningException jobExecutionNotRunningException) {
            throw new JobException(id, jobExecutionNotRunningException.getMessage(), JobException.ErrorCode.NOT_RUNNING);
        } catch (NoSuchJobExecutionException noSuchJobExecutionException) {
            throw new JobException(id, noSuchJobExecutionException.getMessage(), JobException.ErrorCode.NO_EXECUTION);
        }
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

    @Autowired
    public void setJobOperator(JobOperator jobOperator) {
        this.jobOperator = jobOperator;
    }
}
