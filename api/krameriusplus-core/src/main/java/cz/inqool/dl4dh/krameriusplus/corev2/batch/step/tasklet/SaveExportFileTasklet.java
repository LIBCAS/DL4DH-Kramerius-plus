package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.tasklet;

import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.corev2.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.corev2.file.FileService;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.export.Export;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.export.ExportStore;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.ExecutionContextKey.ZIPPED_FILE;
import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.EXPORT_ID;

@Component
@StepScope
public class SaveExportFileTasklet implements Tasklet {

    private FileService fileService;

    private ExportStore exportStore;

    @Value("#{jobParameters['" + EXPORT_ID + "']}")
    private String exportId;

    private Path zippedFile;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Export export = exportStore.findById(exportId)
                .orElseThrow(() -> new MissingObjectException(Export.class, exportId));

        try (InputStream inputStream = Files.newInputStream(zippedFile)) {
            FileRef fileRef = fileService.create(
                    inputStream,
                    Files.size(zippedFile),
                    zippedFile.getFileName().toString(),
                    "application/zip");
            export.setFileRef(fileRef);
            exportStore.save(export);
        }

        return RepeatStatus.FINISHED;
    }

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @Autowired
    public void setExportStore(ExportStore exportStore) {
        this.exportStore = exportStore;
    }

    @Autowired
    public void setZippedFile(@Value("#{jobExecutionContext['" + ZIPPED_FILE + "']}") String zippedFilePath) {
        this.zippedFile = Path.of(zippedFilePath);
    }
}
