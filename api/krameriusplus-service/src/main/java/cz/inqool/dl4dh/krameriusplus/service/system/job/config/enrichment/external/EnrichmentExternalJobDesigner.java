package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.PartitionAggregator;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.PublicationTaskPartitioner;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.StepContainer;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.EnrichmentJobDesigner;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SyncTaskExecutor;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob.ENRICHMENT_EXTERNAL;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.ENRICH_PAGES_ALTO;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.ENRICH_PUBLICATION_MODS;

@Configuration
public class EnrichmentExternalJobDesigner extends EnrichmentJobDesigner {

    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step enrichPagesAltoMaster(StepContainer stepContainer,
                                      PublicationTaskPartitioner publicationTaskPartitioner,
                                      PartitionAggregator partitionAggregator) {
        return stepBuilderFactory.get(ENRICH_PAGES_ALTO + "-MASTER")
                .partitioner(ENRICH_PAGES_ALTO, publicationTaskPartitioner)
                .step(stepContainer.getStep(ENRICH_PAGES_ALTO))
                .taskExecutor(new SyncTaskExecutor())
                .aggregator(partitionAggregator)
                .build();
    }

    @Bean
    public Job enrichExternalJob(@Qualifier("enrichPagesAltoMaster") Step enrichPagesAltoStep) {
        return super.getJobBuilder()
                .next(enrichPagesAltoStep)
                .next(stepContainer.getStep(ENRICH_PUBLICATION_MODS))
                .build();
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Override
    public String getJobName() {
        return ENRICHMENT_EXTERNAL.name();
    }
}
