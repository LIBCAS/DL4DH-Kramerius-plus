package cz.inqool.dl4dh.krameriusplus.core.batch.step.tasklet;

import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.api.exception.TeiEnrichmentException;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.core.enricher.tei.TeiMessenger;
import cz.inqool.dl4dh.krameriusplus.core.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.file.FileService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.api.exception.TeiEnrichmentException.ErrorCode.MISSING_TEI_HEADER_FILE;
import static cz.inqool.dl4dh.krameriusplus.core.batch.step.ExecutionContextKey.TEI_SESSION_ID;
import static cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey.PUBLICATION_ID;
import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

@Component
@StepScope
public class ExportTeiPrepareTasklet implements Tasklet {

    @Value("#{jobParameters['" + PUBLICATION_ID + "']}")
    private String publicationId;

    private PublicationStore publicationStore;

    private FileService fileService;

    private TeiMessenger teiMessenger;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Publication publication = publicationStore.findById(publicationId)
                .orElseThrow(() -> new MissingObjectException(Publication.class, publicationId));

        notNull(publication.getTeiHeaderFileId(),
                () -> new TeiEnrichmentException("Publication '" + publicationId + " has no teiHeaderFileId field.",
                        MISSING_TEI_HEADER_FILE));
        FileRef fileRef = fileService.find(publication.getTeiHeaderFileId());
        notNull(fileRef, () -> new TeiEnrichmentException("Publication '" + publicationId + ", file with fileId: "
                + publication.getTeiHeaderFileId() + " not found.", MISSING_TEI_HEADER_FILE));

        String sessionId = teiMessenger.startMerge(fileRef.open());

        chunkContext.getStepContext().getStepExecutionContext().put(TEI_SESSION_ID, sessionId);

        return RepeatStatus.FINISHED;
    }

    @Autowired
    public void setTeiMessenger(TeiMessenger teiMessenger) {
        this.teiMessenger = teiMessenger;
    }

    @Autowired
    public void setPublicationStore(PublicationStore publicationStore) {
        this.publicationStore = publicationStore;
    }

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}
