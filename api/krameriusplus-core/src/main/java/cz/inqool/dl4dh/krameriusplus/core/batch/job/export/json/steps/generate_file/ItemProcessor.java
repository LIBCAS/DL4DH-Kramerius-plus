package cz.inqool.dl4dh.krameriusplus.core.batch.job.export.json.steps.generate_file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.batch.job.export.json.JsonExportingSteps;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import lombok.NonNull;
import org.apache.http.entity.ContentType;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

@StepScope
@Named(JsonExportingSteps.GenerateFileStep.PROCESSOR_NAME)
@Component
class ItemProcessor implements org.springframework.batch.item.ItemProcessor<Publication, FileRef> {

    private final ObjectMapper objectMapper;

    private final FileService fileService;

    private StepExecution stepExecution;

    @Autowired
    ItemProcessor(ObjectMapper objectMapper, FileService fileService) {
        this.objectMapper = objectMapper;
        this.fileService = fileService;
    }

    @Override
    public FileRef process(@NonNull Publication publication) {
        try {
            byte[] content = objectMapper.writeValueAsBytes(publication);

            try (InputStream is = new ByteArrayInputStream(content)) {
                FileRef fileRef = fileService.create(
                        is,
                        content.length,
                        ExportFormat.JSON.getFileName(publication.getId()),
                        ContentType.APPLICATION_JSON.getMimeType());

                stepExecution.getExecutionContext().put("fileRefId", fileRef.getId());

                return fileRef;
            }
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Could not write publication to string.");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }
}
