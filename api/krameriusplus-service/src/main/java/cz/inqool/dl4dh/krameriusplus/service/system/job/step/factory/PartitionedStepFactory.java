package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.service.system.job.step.OrderedSimpleStepExecutionSplitter;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.PublicationTaskPartitioner;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.listener.StepFailureListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.PartitionStepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SyncTaskExecutor;

public abstract class PartitionedStepFactory implements StepFactory {

    private StepBuilderFactory stepBuilderFactory;

    protected PublicationTaskPartitioner publicationTaskPartitioner;

    private SyncTaskExecutor taskExecutor;

    private StepFailureListener stepFailureListener;

    private JobRepository jobRepository;

    protected PartitionStepBuilder getBuilder() {
        return stepBuilderFactory.get(getStepName())
                .partitioner(getPartitionedStep().getName(), publicationTaskPartitioner)
                .splitter(new OrderedSimpleStepExecutionSplitter(jobRepository, getPartitionedStep().getName(), publicationTaskPartitioner))
                .step(getPartitionedStep())
                .taskExecutor(taskExecutor)
                .listener(stepFailureListener);
    }

    protected abstract String getStepName();

    protected abstract Step getPartitionedStep();

    @Autowired
    public void setPublicationTaskPartitioner(PublicationTaskPartitioner publicationTaskPartitioner) {
        this.publicationTaskPartitioner = publicationTaskPartitioner;
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Autowired
    public void setTaskExecutor(SyncTaskExecutor syncTaskExecutor) {
        this.taskExecutor = syncTaskExecutor;
    }

    @Autowired
    public void setStepFailureListener(StepFailureListener stepFailureListener) {
        this.stepFailureListener = stepFailureListener;
    }

    @Autowired
    public void setJobRepository(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }
}
