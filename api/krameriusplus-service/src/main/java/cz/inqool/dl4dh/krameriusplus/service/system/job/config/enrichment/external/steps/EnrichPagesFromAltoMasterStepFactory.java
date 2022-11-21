package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.steps;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.OrderedSimpleStepExecutionSplitter;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.PublicationTaskPartitioner;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.AbstractStepFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.ENRICH_PAGES_ALTO;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.ENRICH_PAGES_ALTO_MASTER;

@Component
public class EnrichPagesFromAltoMasterStepFactory extends AbstractStepFactory {

    private final PublicationTaskPartitioner publicationTaskPartitioner;

    private final EnrichPagesFromAltoStepFactory enrichPagesFromAltoStepFactory;

    private final JobRepository jobRepository;

    @Autowired
    public EnrichPagesFromAltoMasterStepFactory(PublicationTaskPartitioner publicationTaskPartitioner,
                                                EnrichPagesFromAltoStepFactory enrichPagesFromAltoStepFactory,
                                                JobRepository jobRepository) {
        this.publicationTaskPartitioner = publicationTaskPartitioner;
        this.enrichPagesFromAltoStepFactory = enrichPagesFromAltoStepFactory;
        this.jobRepository = jobRepository;
    }

    @Override
    protected String getStepName() {
        return ENRICH_PAGES_ALTO_MASTER;
    }

    @Override
    public Step build() {
        return getBuilder()
                .partitioner(enrichPagesFromAltoStepFactory.build())
                .splitter(new OrderedSimpleStepExecutionSplitter(jobRepository, ENRICH_PAGES_ALTO, publicationTaskPartitioner))
                .taskExecutor(new SyncTaskExecutor())
                .build();
    }
}
