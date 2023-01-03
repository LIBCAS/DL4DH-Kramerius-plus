package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.tasklet;

import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.corev2.enricher.tei.TeiMessenger;
import cz.inqool.dl4dh.krameriusplus.corev2.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.corev2.file.FileService;
import org.apache.http.entity.ContentType;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.PUBLICATION_ID;

@Component
@StepScope
public class EnrichTeiPublicationTasklet implements Tasklet {

    @Value("#{jobParameters['" + PUBLICATION_ID + "']}")
    private String publicationId;

    private PublicationStore publicationStore;

    private FileService fileService;

    private TeiMessenger teiMessenger;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Publication publication = publicationStore.findById(publicationId)
                .orElseThrow(() -> new MissingObjectException(Publication.class, publicationId));

        String tei = teiMessenger.convertHeader(publication);
        byte[] teiBytes = tei.getBytes(StandardCharsets.UTF_8);

        try (InputStream is = new ByteArrayInputStream(teiBytes)) {
            FileRef fileRef = fileService.create(is, teiBytes.length, publication.getId(), ContentType.APPLICATION_XML.getMimeType());
            publication.setTeiHeaderFileId(fileRef.getId());

            publicationStore.save(publication);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return RepeatStatus.FINISHED;
    }

    @Autowired
    public void setPublicationStore(PublicationStore publicationStore) {
        this.publicationStore = publicationStore;
    }

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @Autowired
    public void setTeiMessenger(TeiMessenger teiMessenger) {
        this.teiMessenger = teiMessenger;
    }
}
