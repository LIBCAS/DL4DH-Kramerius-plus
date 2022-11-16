package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.AbstractStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.steps.EnrichPagesFromAltoStepFactory;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.ENRICH_PAGES_ALTO;

@Component
public class MasterStepFactory extends AbstractStepFactory {

    private final PublicationTaskPartitioner publicationTaskPartitioner;

    private final EnrichPagesFromAltoStepFactory enrichPagesFromAltoStepFactory;

    private final TaskExecutor taskExecutor;

    @Autowired
    public MasterStepFactory(PublicationTaskPartitioner publicationTaskPartitioner,
                             EnrichPagesFromAltoStepFactory enrichPagesFromAltoStepFactory, TaskExecutor taskExecutor) {
        this.publicationTaskPartitioner = publicationTaskPartitioner;
        this.enrichPagesFromAltoStepFactory = enrichPagesFromAltoStepFactory;
        this.taskExecutor = taskExecutor;
    }

    @Override
    protected String getStepName() {
        return ENRICH_PAGES_ALTO;
    }

    @Override
    public Step build() {
        return stepBuilderFactory.get(ENRICH_PAGES_ALTO)
                .partitioner("TEMPSTEPNAME", publicationTaskPartitioner)
                .step(enrichPagesFromAltoStepFactory.build())
                .taskExecutor(taskExecutor())
                .build();
    }

    public TaskExecutor taskExecutor(){
        return new SyncTaskExecutor();
    }
}
