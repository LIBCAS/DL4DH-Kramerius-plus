package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.SKIP_TOLERANCE;

@Component
@StepScope
public class GeneralExceptionSkipPolicy implements SkipPolicy, StepExecutionListener {

    private Integer maxSkips;

    private final List<Throwable> stepRunExceptions = new ArrayList<>();

    public GeneralExceptionSkipPolicy(@Value("#{jobParameters['" + SKIP_TOLERANCE + "']}") Integer skipTolerance) {
        maxSkips = skipTolerance;
    }

    @Override
    public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {
        if (skipCount < Integer.MAX_VALUE) {
            stepRunExceptions.add(t);
        }

        throw new SkipLimitExceededException(skipCount, t);
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        // do nothing
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        stepRunExceptions.forEach(stepExecution::addFailureException);
        return null;
    }
}
