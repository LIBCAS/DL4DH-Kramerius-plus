package cz.inqool.dl4dh.krameriusplus.corev2.job.runner;

import cz.inqool.dl4dh.krameriusplus.api.exception.JobException;
import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.corev2.job.JobContainer;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstanceService;
import cz.inqool.dl4dh.krameriusplus.corev2.job.listener.KrameriusJobListenerContainer;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
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

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void run(String krameriusJobInstanceId) {
        KrameriusJobInstance jobInstance = jobService.find(krameriusJobInstanceId);
        notNull(jobInstance, () -> new MissingObjectException(KrameriusJobInstance.class, krameriusJobInstanceId));

        try {
            jobLauncher.run(jobContainer.getJob(jobInstance.getJobType()), jobInstance.getJobParameters());
        } catch (JobInstanceAlreadyCompleteException e) {
            throw new JobException(
                    "KrameriusJobInstance with id=" + krameriusJobInstanceId + " has already completed.",
                    IS_COMPLETED,
                    e);
        } catch (JobExecutionAlreadyRunningException e) {
            throw new JobException(
                    "KrameriusJobInstance with id=" + krameriusJobInstanceId + " is already running.",
                    IS_RUNNING,
                    e);
        } catch (JobParametersInvalidException e) {
            throw new JobException(
                    "KrameriusJobInstance with id=" + krameriusJobInstanceId + " have invalid JobParameters.",
                    INVALID_JOB_PARAMETERS,
                    e);
        } catch (JobRestartException e) {
            throw new JobException(
                    "KrameriusJobInstance with id=" + krameriusJobInstanceId + " cannot be restarted.",
                    NOT_RESTARTABLE,
                    e);
        } finally {
            listenerContainer.applyAfterJobListeners(jobInstance);
        }
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
}
