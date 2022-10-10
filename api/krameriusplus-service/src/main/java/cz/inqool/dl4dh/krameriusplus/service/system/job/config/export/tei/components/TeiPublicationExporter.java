package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.tei.components;


import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObjectContext;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.tei.TeiExportParams;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.DataProvider;
import cz.inqool.dl4dh.krameriusplus.service.system.exporter.TeiExporter;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.DIRECTORY;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.TEI_EXPORT_PARAMS;

/**
 * Resolve directory and create tei file
 *
 * @author Filip Kollar
 */
@Component
@StepScope
public class TeiPublicationExporter implements StepExecutionListener, ItemWriter<Publication> {

    private String exportDirectory;

    private final DataProvider dataProvider;

    private final TeiExporter teiExporter;

    private final ObjectMapper objectMapper;

    private JobParameters jobParameters;

    @Autowired
    public TeiPublicationExporter(DataProvider dataProvider, TeiExporter teiExporter, ObjectMapper objectMapper) {
        this.dataProvider = dataProvider;
        this.teiExporter = teiExporter;
        this.objectMapper = objectMapper;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        exportDirectory = stepExecution.getJobExecution().getExecutionContext().getString(DIRECTORY);
        jobParameters = stepExecution.getJobParameters();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

    @Override
    public void write(List<? extends Publication> items) throws Exception {
        TeiExportParams params = objectMapper.readValue(jobParameters.getString(TEI_EXPORT_PARAMS), TeiExportParams.class);

        for (Publication publication : items) {
            Path targetPath = resolvePathFromContext(publication.getId());
            Files.createDirectories(targetPath);
            teiExporter.export(publication.getId(), params, targetPath.resolve(publication.getId().substring(5) + ".xml"));
        }
    }

    private Path resolvePathFromContext(String id) {
        Publication publicationWithContext = (Publication) dataProvider.getDigitalObject(id);
        List<DigitalObjectContext> context = publicationWithContext.getContext();
        context = context.subList(0, context.size() - 1);
        Path resultPath = Path.of(exportDirectory);

        for (DigitalObjectContext digitalObjectContext : context) {
            resultPath = resultPath.resolve(digitalObjectContext.getPid().substring(5));
        }

        return resultPath;
    }
}
