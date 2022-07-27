package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.listener;

import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.JobEventService;
import lombok.NonNull;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * StepExecutionListener saves failure into JobExecution for later use
 *
 * @author Filip Kollar
 */
@StepScope
@Component
public class StepFailureListener implements StepExecutionListener {
    private final JobEventService jobEventService;

    @Autowired
    public StepFailureListener(JobEventService jobEventService) {
        this.jobEventService = jobEventService;
    }

    @Override
    public void beforeStep(@NonNull StepExecution stepExecution) {
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        List<Throwable> failures = stepExecution.getFailureExceptions();
        if (!failures.isEmpty()) {
            stepExecution.getJobExecution().addFailureException(failures.get(0));
            JobEvent jobEvent = jobEventService.getStore()
                    .find(stepExecution.getJobParameters().getString(JobParameterKey.JOB_EVENT_ID));
            jobEvent.getDetails().setLastExecutionFailure(failures.get(0).getMessage());
            jobEventService.updateRunningJob(jobEvent.getId(), jobEvent.getInstanceId(), stepExecution.getJobExecutionId(), failures.get(0).getMessage());

        }
        return null;
    }
}
