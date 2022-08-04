package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.DataProvider;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.PUBLICATION_TITLE;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ID;

@Component
@StepScope
public class PreparePublicationMetadataTasklet implements Tasklet {

    private final DataProvider dataProvider;

    @Autowired
    public PreparePublicationMetadataTasklet(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        ExecutionContext stepContext = stepExecution.getExecutionContext();
        String publicationId = stepExecution.getJobExecution().getJobParameters().getString(PUBLICATION_ID);

        Publication publication = (Publication) dataProvider.getDigitalObject(publicationId);
        stepContext.putString(PUBLICATION_TITLE, publication.getTitle());

        return RepeatStatus.FINISHED;
    }
}
