package cz.inqool.dl4dh.krameriusplus.core.batch.step.tasklet;

import cz.inqool.dl4dh.krameriusplus.core.batch.step.PublicationProvider;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey;
import cz.inqool.dl4dh.krameriusplus.core.utils.Utils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.batch.step.ExecutionContextKey.PUBLICATION_MODEL;
import static cz.inqool.dl4dh.krameriusplus.core.batch.step.ExecutionContextKey.PUBLICATION_TITLE;

@Component
@StepScope
public class PrepareExportMetadataTasklet implements Tasklet {

    private PublicationProvider publicationProvider;

    @Value("#{jobParameters['" + JobParameterKey.PUBLICATION_ID + "']}")
    private String publicationId;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        Utils.notNull(publicationId, () -> new IllegalStateException("Publication ID not in jobParameters"));

        Publication publication = publicationProvider.find(publicationId);

        ExecutionContext stepContext = chunkContext.getStepContext().getStepExecution().getExecutionContext();
        stepContext.put(PUBLICATION_TITLE, publication.getTitle());
        stepContext.put(PUBLICATION_MODEL, publication.getModel().getName());

        return RepeatStatus.FINISHED;
    }

    @Autowired
    public void setPublicationProvider(PublicationProvider publicationProvider) {
        this.publicationProvider = publicationProvider;
    }
}
