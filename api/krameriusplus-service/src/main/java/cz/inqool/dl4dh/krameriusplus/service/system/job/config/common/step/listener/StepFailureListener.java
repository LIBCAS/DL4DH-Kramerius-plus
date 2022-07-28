package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.listener;

import cz.inqool.dl4dh.krameriusplus.core.domain.exception.GeneralException;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
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

    private static final String UNEXPECTED_EXCEPTION_CODE = "UNEXPECTED_ERROR_OCCURRED";

    private final JobEventService jobEventService;

    @Autowired
    public StepFailureListener(JobEventService jobEventService) {
        this.jobEventService = jobEventService;
    }

    @Override
    public void beforeStep(@NonNull StepExecution stepExecution) {
        // no-op
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        List<Throwable> failures = stepExecution.getFailureExceptions();

        if (!failures.isEmpty()) {
            Throwable exception = failures.get(0);
            String errorCode = exception instanceof GeneralException ?
                    ((GeneralException) exception).getErrorCode().name() : UNEXPECTED_EXCEPTION_CODE;

            JobEventDto jobEvent = jobEventService.find(stepExecution.getJobParameters().getString(JobParameterKey.JOB_EVENT_ID));
            jobEvent.getDetails().setLastExecutionExitCode(errorCode);
            jobEvent.getDetails().setLastExecutionExitDescription(exception.toString());
            jobEventService.update(jobEvent);

            return new ExitStatus(errorCode);
        }

        return null;
    }

}
