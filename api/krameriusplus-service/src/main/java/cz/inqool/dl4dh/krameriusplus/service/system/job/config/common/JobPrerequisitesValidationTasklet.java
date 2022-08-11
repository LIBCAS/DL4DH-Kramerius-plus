package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common;

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

import java.util.Set;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.KRAMERIUS_JOB;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ID;

@Component
@StepScope
public class JobPrerequisitesValidationTasklet implements Tasklet {

    private final JobEventStore jobEventStore;

    @Autowired
    public JobPrerequisitesValidationTasklet(JobEventStore jobEventStore) {
        this.jobEventStore = jobEventStore;
    }

    @Override
    public RepeatStatus execute(@NonNull StepContribution contribution,@NonNull ChunkContext chunkContext) throws Exception {
        JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobParameters();

        String publicationId = jobParameters.getString(PUBLICATION_ID);

        Set<KrameriusJob> prerequisites = KrameriusJob.valueOf(jobParameters.getString(KRAMERIUS_JOB)).getDependentOn();

        for (KrameriusJob prerequisite: prerequisites) {
            Long count = jobEventStore.getDependency(publicationId, prerequisite);
            if (count.equals(0L))  {
                throw new ValidationException(String.format("No completed jobs of type %s for publicationId %s", prerequisite, publicationId),
                        ValidationException.ErrorCode.DEPENDENCY_ERROR);
            }
        }

        return RepeatStatus.FINISHED;
    }
}
