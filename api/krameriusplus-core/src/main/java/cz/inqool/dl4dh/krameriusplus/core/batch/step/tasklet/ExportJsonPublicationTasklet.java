package cz.inqool.dl4dh.krameriusplus.core.batch.step.tasklet;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.batch.step.wrapper.DigitalObjectExport;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.core.exporter.DigitalObjectExporter;
import cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.batch.step.ExecutionContextKey.DIRECTORY;

@Component
@StepScope
public class ExportJsonPublicationTasklet implements Tasklet {

    private ModsProvider modsProvider;

    private PublicationStore publicationStore;

    private DigitalObjectExporter exporter;

    private ObjectMapper objectMapper;

    private Path directory;

    @Value("#{jobParameters['" + JobParameterKey.PUBLICATION_ID + "']}")
    private String publicationId;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Publication publication = publicationStore.findById(publicationId)
                .orElseThrow(() -> new MissingObjectException(Publication.class, publicationId));

        // clear some fields which we do not want to export
        publication.setChildren(null);
        publication.setPages(null);
        publication.setTeiHeaderFileId(null);
        publication.setIsRootEnrichment(null);

        publication.setModsMetadata(modsProvider.getMods(publicationId));

        exporter.export(List.of(new DigitalObjectExport(publication, objectMapper.writeValueAsString(publication),
                publication.getModel() + "_" + publicationId.substring(5) + ".json")),
                directory);

        return RepeatStatus.FINISHED;
    }

    @Autowired
    public void setPublicationStore(PublicationStore publicationStore) {
        this.publicationStore = publicationStore;
    }

    @Autowired
    public void setExporter(DigitalObjectExporter exporter) {
        this.exporter = exporter;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setDirectory(@Value("#{jobExecutionContext['" + DIRECTORY + "']}") String directory) {
        this.directory = Path.of(directory);
    }

    @Autowired
    public void setModsProvider(ModsProvider modsProvider) {
        this.modsProvider = modsProvider;
    }
}
