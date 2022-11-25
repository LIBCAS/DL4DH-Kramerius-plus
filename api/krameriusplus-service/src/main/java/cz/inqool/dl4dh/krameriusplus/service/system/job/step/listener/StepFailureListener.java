package cz.inqool.dl4dh.krameriusplus.service.system.job.step.listener;

import cz.inqool.dl4dh.krameriusplus.core.domain.exception.GeneralException;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
import cz.inqool.dl4dh.krameriusplus.service.system.jobevent.JobEventService;
import lombok.NonNull;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.JOB_EVENT_ID;

/**
 * StepExecutionListener saves failure into JobExecution for later use
 *
 * @author Filip Kollar
 */
@StepScope
@Component
public class StepFailureListener implements StepExecutionListener {

    private static final String UNEXPECTED_EXCEPTION_CODE = "UNEXPECTED_ERROR_OCCURRED";

    private static final String STOP_EXCEPTION_CODE = "STEP_STOPPED";

    @Value("#{jobParameters['" + JOB_EVENT_ID + "']}")
    private String jobEventId;

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
            String errorCode;

            if (exception instanceof GeneralException) {
                errorCode = ((GeneralException) exception).getErrorCode().name();
            }
            else if (exception instanceof JobInterruptedException) {
                errorCode = STOP_EXCEPTION_CODE;
            }
            else {
                errorCode = UNEXPECTED_EXCEPTION_CODE;
            }

            for (Throwable throwable : failures) {
                jobEventService.saveError(jobEventId, stepExecution.getId(), throwable);
            }

            JobEventDto jobEvent = jobEventService.find(jobEventId);
            jobEvent.getDetails().setLastExecutionExitCode(errorCode);
            jobEvent.getDetails().setLastExecutionExitDescription(exception.toString());
            jobEventService.update(jobEvent);

            return new ExitStatus(errorCode);
        }

        return null;
    }

}
