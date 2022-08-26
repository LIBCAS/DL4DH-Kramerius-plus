package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.tei.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.TeiExportParams;
import cz.inqool.dl4dh.krameriusplus.service.system.exporter.TeiExporter;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.DIRECTORY;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ID;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.TEI_EXPORT_PARAMS;

@Component
@StepScope
public class ExportTeiTasklet implements Tasklet {

    private final ObjectMapper objectMapper;

    private final TeiExporter exporter;

    @Autowired
    public ExportTeiTasklet(ObjectMapper objectMapper, TeiExporter exporter) {
        this.objectMapper = objectMapper;
        this.exporter = exporter;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String publicationId = (String) chunkContext.getStepContext().getJobParameters().get(PUBLICATION_ID);
        TeiExportParams params = objectMapper.readValue((String) chunkContext.getStepContext().getJobParameters().get(TEI_EXPORT_PARAMS), TeiExportParams.class);
        Path parentDirectory = Path.of((String) chunkContext.getStepContext().getJobExecutionContext().get(DIRECTORY));
        Path teiFile = Files.createFile(parentDirectory.resolve(publicationId.substring(5) + ".xml"));

        exporter.export(publicationId, params, teiFile);

        return RepeatStatus.FINISHED;
    }
}
