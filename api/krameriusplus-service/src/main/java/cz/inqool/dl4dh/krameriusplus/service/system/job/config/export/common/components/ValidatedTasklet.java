package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.HashSet;
import java.util.Set;

public abstract class ValidatedTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        validateKeyPresence(chunkContext);
        return executeValidatedTasklet(contribution, chunkContext);
    }

    private void validateKeyPresence(ChunkContext chunkContext) {
        ExecutionContext jobExecutionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();

        getRequiredExecutionContextKeys().forEach(key -> {
            if (!jobExecutionContext.containsKey(key)) {
                throw new IllegalStateException("Job: " + chunkContext.getStepContext().getJobName() + " failed " +
                        this.getClass().getSimpleName() + " missing required key: " + key);
            }
        });
    }

    protected abstract RepeatStatus executeValidatedTasklet(StepContribution contribution, ChunkContext chunkContext) throws Exception;

    protected Set<String> getRequiredExecutionContextKeys() {
        return new HashSet<>();
    }
}
