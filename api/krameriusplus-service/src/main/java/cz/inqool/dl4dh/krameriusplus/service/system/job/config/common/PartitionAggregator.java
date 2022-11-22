package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.partition.support.StepExecutionAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_SKIP_COUNT;

@Component
@JobScope
public class PartitionAggregator implements StepExecutionAggregator {

    @Value("#{jobParameters['" + PUBLICATION_SKIP_COUNT + "']}")
    private Long maxFailures = 0L;

    @Override
    public void aggregate(StepExecution result, Collection<StepExecution> executions) {
        long failedCount  = executions.stream().filter(execution -> !execution.getExitStatus().equals(ExitStatus.COMPLETED)).count();

        result.setExitStatus(failedCount > maxFailures ? ExitStatus.FAILED : ExitStatus.COMPLETED);
    }
}
