package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.ndk;

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

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob.ENRICHMENT_NDK;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.ENRICH_PAGES_NDK;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.PREPARE_PUBLICATION_NDK;

@Configuration
public class EnrichNdkJobDesigner extends EnrichmentJobDesigner {

    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step enrichPagesNdkMaster(StepContainer stepContainer, PublicationTaskPartitioner publicationTaskPartitioner) {
        return stepBuilderFactory.get(ENRICH_PAGES_NDK + "-MASTER")
                .partitioner(ENRICH_PAGES_NDK, publicationTaskPartitioner)
                .step(stepContainer.getStep(ENRICH_PAGES_NDK))
                .taskExecutor(new SyncTaskExecutor())
                .build();
    }

    @Bean
    public Job enrichNdkJob(@Qualifier("enrichPagesNdkMaster") Step enrichNdkMasterStep) {
        return super.getJobBuilder()
                .next(stepContainer.getStep(PREPARE_PUBLICATION_NDK))
                .next(enrichNdkMasterStep)
                .build();
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Override
    public String getJobName() {
        return ENRICHMENT_NDK.name();
    }
}
