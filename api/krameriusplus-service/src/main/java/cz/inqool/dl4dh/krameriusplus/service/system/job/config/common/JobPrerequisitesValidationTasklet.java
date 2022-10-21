package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common;

import cz.inqool.dl4dh.krameriusplus.core.domain.exception.ValidationException;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObjectContext;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventStore;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.DataProvider;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.ValidatedTasklet;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.KRAMERIUS_JOB;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ID;

@Component
@StepScope
public class JobPrerequisitesValidationTasklet extends ValidatedTasklet {

    private final JobEventStore jobEventStore;

    private final DataProvider dataProvider;

    @Autowired
    public JobPrerequisitesValidationTasklet(JobEventStore jobEventStore, DataProvider dataProvider) {
        this.jobEventStore = jobEventStore;
        this.dataProvider = dataProvider;
    }

    @Override
    protected RepeatStatus executeValidatedTasklet(@NonNull StepContribution contribution, @NonNull ChunkContext chunkContext) throws Exception {
        JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobParameters();

        String publicationId = jobParameters.getString(PUBLICATION_ID);
        // Workaround for merge job since it is the only job not tied to a publication
        if (publicationId == null) {
            return RepeatStatus.FINISHED;
        }
        List<String> relevantContext =  getRelevantContext((Publication) dataProvider.getDigitalObject(publicationId));

        Set<KrameriusJob> prerequisites = KrameriusJob.valueOf(jobParameters.getString(KRAMERIUS_JOB)).getDependentOn();

        for (KrameriusJob prerequisite: prerequisites) {
            Long count = jobEventStore.getDependency(relevantContext, prerequisite);
            if (count.equals(0L))  {
                throw new ValidationException(String.format("No completed jobs of type %s for publicationId %s", prerequisite, publicationId),
                        ValidationException.ErrorCode.DEPENDENCY_ERROR);
            }
        }

        return RepeatStatus.FINISHED;
    }

    private List<String> getRelevantContext(Publication publication) {
        Collections.reverse(publication.getContext());
        return publication.getContext().stream()
                .map(DigitalObjectContext::getPid)
                .dropWhile(id -> !id.equals(publication.getId()))
                .collect(Collectors.toList());
    }
}
