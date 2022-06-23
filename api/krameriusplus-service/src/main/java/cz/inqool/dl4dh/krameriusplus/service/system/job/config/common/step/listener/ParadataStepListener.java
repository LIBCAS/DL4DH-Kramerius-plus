package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublicationService;
import cz.inqool.dl4dh.krameriusplus.core.system.paradata.EnrichmentParadata;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.PARADATA;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ID;

@Component
@StepScope
public class ParadataStepListener implements StepExecutionListener {

    private final ObjectMapper objectMapper;

    private final PublicationService publicationService;

    @Autowired
    public ParadataStepListener(ObjectMapper objectMapper, PublicationService publicationService) {
        this.objectMapper = objectMapper;
        this.publicationService = publicationService;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        EnrichmentParadata paradata = (EnrichmentParadata) stepExecution.getExecutionContext().get(PARADATA);

        if (paradata != null) {
            paradata.setProcessingStarted(stepExecution.getStartTime().toInstant());
            paradata.setProcessingFinished(Instant.now());

            Publication publication = publicationService.find(stepExecution.getJobExecution().getJobParameters().getString(PUBLICATION_ID));
            publication.getParadata().put(paradata.getExternalSystem(), paradata);

            publicationService.save(publication);

            stepExecution.getExecutionContext().remove(PARADATA);
        }

        return null;
    }
}
