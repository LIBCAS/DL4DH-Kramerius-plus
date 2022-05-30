package cz.inqool.dl4dh.krameriusplus.service.system.job.jobconfig.common;

import cz.inqool.dl4dh.krameriusplus.service.system.job.jobconfig.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.JobEventStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.jobconfig.common.JobStep.ENRICHMENT_VALIDATION;

@Configuration
@Slf4j
public class EnrichmentValidationStep {

    private final StepBuilderFactory stepBuilderFactory;

    private final JobEventStore jobEventStore;

    @Autowired
    public EnrichmentValidationStep(StepBuilderFactory stepBuilderFactory, JobEventStore jobEventStore) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.jobEventStore = jobEventStore;
    }

    @Bean
    public Step validationStep() {
        return stepBuilderFactory.get(ENRICHMENT_VALIDATION)
                .tasklet(validationTasklet())
                .build();
    }

    @Bean
    @StepScope
    public Tasklet validationTasklet() {
        return (contribution, chunkContext) -> {
            JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobParameters();

            String publicationId = jobParameters.getString("publicationId");
            String thisJobEventId = jobParameters.getString("jobEventId");
            KrameriusJob krameriusJob = KrameriusJob.valueOf(jobParameters.getString("krameriusJob"));

            boolean override = Boolean.parseBoolean(jobParameters.getString("override"));
            if (!override && jobEventStore.existsOtherJobs(publicationId, thisJobEventId, krameriusJob)) {
                throw new IllegalStateException("Job of type '" + krameriusJob + "' for publication '" + publicationId + "' already exists and 'override' is set to false.");
            }
            // if exists jobEvent in state COMPLETED with same KrameriusJob and PublicationId, throw error

            return RepeatStatus.FINISHED;
        };
    }
}
