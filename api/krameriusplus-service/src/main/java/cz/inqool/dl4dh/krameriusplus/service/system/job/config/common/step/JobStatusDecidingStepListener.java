package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_SKIP_COUNT;

@Component
@JobScope
public class JobStatusDecidingStepListener implements StepExecutionListener {

    @Value("#{jobParameters['" + PUBLICATION_SKIP_COUNT + "']}")
    private Integer publicationSkipTolerance = Integer.MAX_VALUE;

    private Integer failedStepCount = 0;

    @Override
    public void beforeStep(StepExecution stepExecution) {

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (stepExecution.getExitStatus().equals(ExitStatus.FAILED)) {
            failedStepCount++;
        }
        else if (stepExecution.getStepName().contains("MASTER")) {
            return failedStepCount > publicationSkipTolerance ? ExitStatus.FAILED : ExitStatus.COMPLETED;
        }

        return null;
    }
}
