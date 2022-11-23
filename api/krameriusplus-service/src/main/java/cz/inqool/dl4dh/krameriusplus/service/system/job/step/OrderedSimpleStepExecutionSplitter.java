package cz.inqool.dl4dh.krameriusplus.service.system.job.step;

import org.springframework.batch.core.*;
import org.springframework.batch.core.partition.StepExecutionSplitter;
import org.springframework.batch.core.partition.support.PartitionNameProvider;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.partition.support.SimpleStepExecutionSplitter;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.*;

/**
 * Reimplementation of SimpleStepExecutionSplitter to keep order of partitions.
 */
public class OrderedSimpleStepExecutionSplitter implements StepExecutionSplitter, InitializingBean {

    private static final String STEP_NAME_SEPARATOR = "_";

    private String stepName;

    private Partitioner partitioner;

    private JobRepository jobRepository;

    /**
     * Construct a {@link SimpleStepExecutionSplitter} from its mandatory
     * properties.
     *
     * @param jobRepository the {@link JobRepository}
     * @param stepName the target step name
     * @param partitioner a {@link Partitioner} to use for generating input
     * parameters
     */
    public OrderedSimpleStepExecutionSplitter(JobRepository jobRepository, String stepName, Partitioner partitioner) {
        this.jobRepository = jobRepository;
        this.partitioner = partitioner;
        this.stepName = stepName;
    }

    /**
     * Check mandatory properties (step name, job repository and partitioner).
     *
     * @see InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.state(jobRepository != null, "A JobRepository is required");
        Assert.state(stepName != null, "A step name is required");
        Assert.state(partitioner != null, "A Partitioner is required");
    }

    /**
     * @see StepExecutionSplitter#getStepName()
     */
    @Override
    public String getStepName() {
        return this.stepName;
    }

    /**
     * @see StepExecutionSplitter#split(StepExecution, int)
     */
    @Override
    public Set<StepExecution> split(StepExecution stepExecution, int gridSize) throws JobExecutionException {

        JobExecution jobExecution = stepExecution.getJobExecution();

        Map<String, ExecutionContext> contexts = getContexts(stepExecution, gridSize);
        Set<StepExecution> set = new LinkedHashSet<>(contexts.size());

        for (Map.Entry<String, ExecutionContext> context : contexts.entrySet()) {

            // Make the step execution name unique and repeatable
            String stepName = this.stepName + STEP_NAME_SEPARATOR + context.getKey();

            StepExecution currentStepExecution = jobExecution.createStepExecution(stepName);

            boolean startable = isStartable(currentStepExecution, context.getValue());

            if (startable) {
                set.add(currentStepExecution);
            }
        }

        jobRepository.addAll(set);

        Set<StepExecution> executions = new LinkedHashSet<>(set.size());
        executions.addAll(set);

        return executions;

    }

    private Map<String, ExecutionContext> getContexts(StepExecution stepExecution, int gridSize) {
        ExecutionContext context = stepExecution.getExecutionContext();
        String key = SimpleStepExecutionSplitter.class.getSimpleName() + ".GRID_SIZE";

        // If this is a restart we must retain the same grid size, ignoring the
        // one passed in...
        int splitSize = (int) context.getLong(key, gridSize);
        context.putLong(key, splitSize);

        Map<String, ExecutionContext> result;
        if (context.isDirty()) {
            // The context changed so we didn't already know the partitions
            jobRepository.updateExecutionContext(stepExecution);
            result = partitioner.partition(splitSize);
        }
        else {
            if (partitioner instanceof PartitionNameProvider) {
                result = new HashMap<>();
                Collection<String> names = ((PartitionNameProvider) partitioner).getPartitionNames(splitSize);
                for (String name : names) {
                    /*
                     * We need to return the same keys as the original (failed)
                     * execution, but the execution contexts will be discarded
                     * so they can be empty.
                     */
                    result.put(name, new ExecutionContext());
                }
            }
            else {
                // If no names are provided, grab the partition again.
                result = partitioner.partition(splitSize);
            }
        }

        return result;
    }

    /**
     * Check if a step execution is startable.
     * @param stepExecution the step execution to check
     * @param context the execution context of the step
     * @return true if the step execution is startable, false otherwise
     * @throws JobExecutionException if unable to check if the step execution is startable
     */
    protected boolean isStartable(StepExecution stepExecution, ExecutionContext context) throws JobExecutionException {

        JobInstance jobInstance = stepExecution.getJobExecution().getJobInstance();
        String stepName = stepExecution.getStepName();
        StepExecution lastStepExecution = jobRepository.getLastStepExecution(jobInstance, stepName);

        boolean isRestart = (lastStepExecution != null && lastStepExecution.getStatus() != BatchStatus.COMPLETED);

        if (isRestart) {
            stepExecution.setExecutionContext(lastStepExecution.getExecutionContext());
        }
        else {
            stepExecution.setExecutionContext(context);
        }

        return shouldStart(stepExecution, lastStepExecution) || isRestart;

    }

    private boolean shouldStart(StepExecution stepExecution, StepExecution lastStepExecution)
            throws JobExecutionException {

        if (lastStepExecution == null) {
            return true;
        }

        BatchStatus stepStatus = lastStepExecution.getStatus();

        if (stepStatus == BatchStatus.UNKNOWN) {
            throw new JobExecutionException("Cannot restart step from UNKNOWN status. "
                    + "The last execution ended with a failure that could not be rolled back, "
                    + "so it may be dangerous to proceed. " + "Manual intervention is probably necessary.");
        }

        if (stepStatus == BatchStatus.COMPLETED) {
            // it's always OK to start again in the same JobExecution
            return isSameJobExecution(stepExecution, lastStepExecution);
        }

        if (stepStatus == BatchStatus.STOPPED || stepStatus == BatchStatus.FAILED) {
            return true;
        }

        if (stepStatus == BatchStatus.STARTED || stepStatus == BatchStatus.STARTING
                || stepStatus == BatchStatus.STOPPING) {
            throw new JobExecutionException(
                    "Cannot restart step from "
                            + stepStatus
                            + " status.  "
                            + "The old execution may still be executing, so you may need to verify manually that this is the case.");
        }

        throw new JobExecutionException("Cannot restart step from " + stepStatus + " status.  "
                + "We believe the old execution was abandoned and therefore has been marked as un-restartable.");

    }

    private boolean isSameJobExecution(StepExecution stepExecution, StepExecution lastStepExecution) {
        return stepExecution.getJobExecutionId().equals(lastStepExecution.getJobExecutionId());
    }

}
