package cz.inqool.dl4dh.krameriusplus.service.system.job.step;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.partition.support.StepExecutionAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ERROR_TOLERANCE;

@Component
@JobScope
public class EnrichmentSuccessDecidingAggregator implements StepExecutionAggregator {

    @Value("#{jobParameters['" + PUBLICATION_ERROR_TOLERANCE + "']}")
    private Integer maxFailures = 0;

    @Override
    public void aggregate(StepExecution result, Collection<StepExecution> executions) {
        long failedCount  = executions.stream().filter(execution -> !execution.getExitStatus().equals(ExitStatus.COMPLETED)).count();
        BatchStatus finalStatus = failedCount > maxFailures ? BatchStatus.FAILED : BatchStatus.COMPLETED;
        ExitStatus exitCode = failedCount > maxFailures ? ExitStatus.FAILED : ExitStatus.COMPLETED;

        for (StepExecution stepExecution : executions) {
            result.setStatus(finalStatus);
            result.setExitStatus(exitCode);
            result.setFilterCount(result.getFilterCount() + stepExecution.getFilterCount());
            result.setProcessSkipCount(result.getProcessSkipCount() + stepExecution.getProcessSkipCount());
            result.setCommitCount(result.getCommitCount() + stepExecution.getCommitCount());
            result.setRollbackCount(result.getRollbackCount() + stepExecution.getRollbackCount());
            result.setReadCount(result.getReadCount() + stepExecution.getReadCount());
            result.setReadSkipCount(result.getReadSkipCount() + stepExecution.getReadSkipCount());
            result.setWriteCount(result.getWriteCount() + stepExecution.getWriteCount());
            result.setWriteSkipCount(result.getWriteSkipCount() + stepExecution.getWriteSkipCount());
        }
    }
}
