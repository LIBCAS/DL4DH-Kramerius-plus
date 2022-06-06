package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.validation;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.JobEventStore;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class EnrichmentValidationTasklet implements Tasklet {

    private final JobEventStore jobEventStore;

    @Autowired
    public EnrichmentValidationTasklet(JobEventStore jobEventStore) {
        this.jobEventStore = jobEventStore;
    }

    @Override
    public RepeatStatus execute(@NonNull StepContribution contribution, ChunkContext chunkContext) {
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
    }
}
