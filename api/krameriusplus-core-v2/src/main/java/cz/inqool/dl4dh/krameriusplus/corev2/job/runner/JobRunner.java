package cz.inqool.dl4dh.krameriusplus.corev2.job.runner;

import cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus;
import cz.inqool.dl4dh.krameriusplus.api.exception.JobException;
import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.corev2.job.JobContainer;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstanceService;
import cz.inqool.dl4dh.krameriusplus.corev2.job.listener.KrameriusJobListenerContainer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static cz.inqool.dl4dh.krameriusplus.api.exception.JobException.ErrorCode.*;
import static cz.inqool.dl4dh.krameriusplus.corev2.utils.Utils.notNull;

@Component
public class JobRunner {

    private KrameriusJobInstanceService jobService;

    private JobContainer jobContainer;

    private KrameriusJobListenerContainer listenerContainer;

    private JobLauncher jobLauncher;

    private JobRepository jobRepository;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void run(String krameriusJobInstanceId) {
        KrameriusJobInstance krameriusJobInstance = jobService.find(krameriusJobInstanceId);
        notNull(krameriusJobInstance, () -> new MissingObjectException(KrameriusJobInstance.class, krameriusJobInstanceId));

        try {
            Job job = jobContainer.getJob(krameriusJobInstance.getJobType());
            JobInstance jobInstance = jobRepository.createJobInstance(job.getName(), krameriusJobInstance.getJobParameters());

            jobService.assignInstance(krameriusJobInstance, jobInstance);
            JobExecution jobExecution = jobLauncher.run(job, krameriusJobInstance.getJobParameters());
            krameriusJobInstance.setExecutionStatus(ExecutionStatus.valueOf(jobExecution.getStatus().toString()));
            jobService.updateStatus(krameriusJobInstance);
        } catch (Exception e) {
            throw createExceptionAndSetStatus(e, krameriusJobInstance);
        } finally {
            listenerContainer.applyAfterJobListeners(krameriusJobInstance);
        }
    }

    private RuntimeException createExceptionAndSetStatus(Exception e, KrameriusJobInstance krameriusJobInstance) {
        RuntimeException exception;

        if (e instanceof JobInstanceAlreadyCompleteException) {
            exception =  new JobException(
                    "KrameriusJobInstance with id=" + krameriusJobInstance.getId()+  " has already completed.",
                    IS_COMPLETED,
                    e);
        } else if (e instanceof JobExecutionAlreadyRunningException) {
            exception = new JobException(
                    "KrameriusJobInstance with id=" + krameriusJobInstance.getId()+ " is already running.",
                    IS_RUNNING,
                    e);
        } else if (e instanceof JobParametersInvalidException) {
            exception = new JobException(
                    "KrameriusJobInstance with id=" + krameriusJobInstance.getId() + " have invalid JobParameters.",
                    INVALID_JOB_PARAMETERS,
                    e);
        } else {
            exception = new JobException(
                    "KrameriusJobInstance with id=" + krameriusJobInstance.getId() + " cannot be restarted.",
                    NOT_RESTARTABLE,
                    e);
        }

        krameriusJobInstance.setExecutionStatus(ExecutionStatus.FAILED);
        return exception;
    }

    @Autowired
    public void setJobService(KrameriusJobInstanceService jobService) {
        this.jobService = jobService;
    }

    @Autowired
    public void setJobContainer(JobContainer jobContainer) {
        this.jobContainer = jobContainer;
    }

    @Autowired
    public void setJobLauncher(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    @Autowired
    public void setListenerContainer(KrameriusJobListenerContainer listenerContainer) {
        this.listenerContainer = listenerContainer;
    }

    @Autowired
    public void setJobRepository(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }
}
