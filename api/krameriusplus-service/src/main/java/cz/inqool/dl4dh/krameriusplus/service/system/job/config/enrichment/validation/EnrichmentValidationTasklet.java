package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.validation;

import cz.inqool.dl4dh.krameriusplus.core.domain.exception.ValidationException;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventStore;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.domain.exception.ValidationException.ErrorCode.ALREADY_EXISTS;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.*;

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

        String publicationId = jobParameters.getString(PUBLICATION_ID);
        String thisJobEventId = jobParameters.getString(JOB_EVENT_ID);
        KrameriusJob krameriusJob = KrameriusJob.valueOf(jobParameters.getString(KRAMERIUS_JOB));

        boolean override = Boolean.parseBoolean(jobParameters.getString(OVERRIDE));

        if (!override && jobEventStore.existsOtherJobs(publicationId, thisJobEventId, krameriusJob)) {
            throw new ValidationException(
                    String.format("Job of type %s for publication %s already exists and 'override' is set to false.",
                            krameriusJob, publicationId), ALREADY_EXISTS);
        }

        return RepeatStatus.FINISHED;
    }
}
