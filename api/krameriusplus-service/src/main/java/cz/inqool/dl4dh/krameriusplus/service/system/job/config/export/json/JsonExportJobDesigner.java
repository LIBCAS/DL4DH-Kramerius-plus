package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.json;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.PublicationTaskPartitioner;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.StepContainer;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.ExportJobDesigner;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SyncTaskExecutor;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.CLEAN_UP_EXPORT;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.CREATE_EXPORT;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.EXPORT_PAGES_JSON;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.EXPORT_PUBLICATION_JSON;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.PREPARE_EXPORT_DIRECTORY;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.PREPARE_PUBLICATION_METADATA;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.ZIP_EXPORT;

@Configuration
public class JsonExportJobDesigner extends ExportJobDesigner {

    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step exportPagesJsonMaster(StepContainer stepContainer, PublicationTaskPartitioner publicationTaskPartitioner) {
        return stepBuilderFactory.get(EXPORT_PAGES_JSON + "-MASTER")
                .partitioner(EXPORT_PAGES_JSON, publicationTaskPartitioner)
                .step(stepContainer.getStep(EXPORT_PAGES_JSON))
                .taskExecutor(new SyncTaskExecutor())
                .build();
    }

    @Bean
    public Job exportJsonJob(@Qualifier("exportPagesJsonMaster") Step exportPagesJsonStep) {
        return getJobBuilder()
                .next(stepContainer.getStep(PREPARE_PUBLICATION_METADATA))
                .next(stepContainer.getStep(PREPARE_EXPORT_DIRECTORY))
                .next(stepContainer.getStep(EXPORT_PUBLICATION_JSON))
                .next(exportPagesJsonStep)
                .next(stepContainer.getStep(ZIP_EXPORT))
                .next(stepContainer.getStep(CREATE_EXPORT))
                .next(stepContainer.getStep(CLEAN_UP_EXPORT))
                .build();
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Override
    public String getJobName() {
        return KrameriusJob.EXPORT_JSON.name();
    }

}
