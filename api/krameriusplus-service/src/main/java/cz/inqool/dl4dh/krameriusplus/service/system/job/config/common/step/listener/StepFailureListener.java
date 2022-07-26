package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.listener;

import lombok.NonNull;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

import java.util.List;

public class StepFailureListener implements StepExecutionListener {
    @Override
    public void beforeStep(@NonNull StepExecution stepExecution) {
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        List<Throwable> failures = stepExecution.getFailureExceptions();
        if (failures.isEmpty()) {
            return ExitStatus.COMPLETED;
        }
        stepExecution.getJobExecution().addFailureException(failures.get(failures.size() - 1));
        return ExitStatus.FAILED;
    }
}
