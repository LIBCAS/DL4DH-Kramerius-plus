package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.tei;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.EnrichmentSuccessDecidingAggregator;
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

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob.ENRICHMENT_TEI;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.ENRICH_PAGES_TEI;

@Configuration
public class EnrichTeiJobDesigner extends EnrichmentJobDesigner {

    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job enrichTeiJob(@Qualifier("enrichPagesTeiMaster") Step masterStep) {
        return super.getJobBuilder()
                .next(masterStep)
                .next(stepContainer.getStep(ENRICH_PAGES_TEI))
                .build();
    }

    @Bean
    public Step enrichPagesTeiMaster(StepContainer stepContainer,
                                     PublicationTaskPartitioner publicationTaskPartitioner,
                                     EnrichmentSuccessDecidingAggregator enrichmentSuccessDecidingAggregator) {
        return stepBuilderFactory.get(ENRICH_PAGES_TEI + "-MASTER")
                .partitioner(ENRICH_PAGES_TEI, publicationTaskPartitioner)
                .step(stepContainer.getStep(ENRICH_PAGES_TEI))
                .taskExecutor(new SyncTaskExecutor())
                .aggregator(enrichmentSuccessDecidingAggregator)
                .build();
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Override
    public String getJobName() {
        return ENRICHMENT_TEI.name();
    }
}
