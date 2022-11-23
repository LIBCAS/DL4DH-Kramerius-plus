package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.service.system.job.step.PublicationTaskPartitioner;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.builder.PartitionStepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SyncTaskExecutor;

public abstract class PartitionedStepFactory implements StepFactory {

    private StepBuilderFactory stepBuilderFactory;

    protected PublicationTaskPartitioner publicationTaskPartitioner;

    private SyncTaskExecutor taskExecutor;

    protected PartitionStepBuilder getBuilder() {
        return stepBuilderFactory.get(getStepName())
                .partitioner(getPartitionedStep().getName(), publicationTaskPartitioner)
                .step(getPartitionedStep())
                .taskExecutor(taskExecutor);
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
}
