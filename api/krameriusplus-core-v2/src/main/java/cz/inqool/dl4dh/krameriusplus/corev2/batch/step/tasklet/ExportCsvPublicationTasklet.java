package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.tasklet;

import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.CsvExporter;
import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.wrapper.DigitalObjectExport;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.corev2.exporter.DigitalObjectExporter;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.ExecutionContextKey.DIRECTORY;
import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.PUBLICATION_ID;

@Component
@StepScope
public class ExportCsvPublicationTasklet implements Tasklet {

    private PublicationStore publicationStore;

    private DigitalObjectExporter exporter;

    // todo: inject delim from context
    private CsvExporter csvExporter = new CsvExporter(',');

    private Path directory;

    @Value("#{jobParameters['" + PUBLICATION_ID + "']}")
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

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        csvExporter.export(publication, byteArrayOutputStream);

        exporter.export(List.of(new DigitalObjectExport(publication, byteArrayOutputStream.toString(StandardCharsets.UTF_8),
                        publication.getModel() + "_" + publicationId.substring(5) + ".csv")),
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
    public void setDirectory(@Value("#{jobExecutionContext['" + DIRECTORY + "']}") String directory) {
        this.directory = Path.of(directory);
    }
}
