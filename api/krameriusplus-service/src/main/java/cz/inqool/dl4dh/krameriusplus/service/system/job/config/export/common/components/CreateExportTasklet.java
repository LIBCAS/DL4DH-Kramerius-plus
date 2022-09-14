package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components;

import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportService;
import cz.inqool.dl4dh.krameriusplus.core.system.export.dto.ExportCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.PUBLICATION_TITLE;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.ZIPPED_FILE;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.JOB_EVENT_ID;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ID;

@Component
@StepScope
public class CreateExportTasklet extends ValidatedTasklet {

    private final FileService fileService;

    private final ExportService exportService;

    @Autowired
    public CreateExportTasklet(FileService fileService, ExportService exportService) {
        this.fileService = fileService;
        this.exportService = exportService;
    }

    @Override
    public RepeatStatus executeValidatedTasklet(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String path = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().getString(ZIPPED_FILE);
        Path zippedFile = Path.of(path);

        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobParameters jobParameters = stepExecution.getJobExecution().getJobParameters();
        ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();

        String jobEventId = jobParameters.getString(JOB_EVENT_ID);
        String publicationId = jobParameters.getString(PUBLICATION_ID);
        String publicationTitle = executionContext.getString(PUBLICATION_TITLE);

        FileRef fileRef;

        try (InputStream is = new FileInputStream(zippedFile.toFile())) {
            fileRef = fileService.create(is,
                    Files.size(zippedFile),
                    zippedFile.getFileName().toString(),
                    "application/zip");
        }

        JobEventDto jobEventDto = new JobEventDto();
        jobEventDto.setId(jobEventId);

        ExportCreateDto exportCreateDto = new ExportCreateDto();
        exportCreateDto.setPublicationId(publicationId);
        exportCreateDto.setPublicationTitle(publicationTitle);
        exportCreateDto.setFileRef(fileRef);
        exportCreateDto.setJobEvent(jobEventDto);

        exportService.create(exportCreateDto);

        return RepeatStatus.FINISHED;
    }

    @Override
    public Set<String> getRequiredExecutionContextKeys() {
        return Set.of(ZIPPED_FILE);
    }
}
