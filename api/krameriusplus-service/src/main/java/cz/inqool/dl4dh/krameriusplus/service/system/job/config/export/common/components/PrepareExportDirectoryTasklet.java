package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobParameterKey.*;

@Component
@StepScope
public class PrepareExportDirectoryTasklet implements Tasklet {

    public static final String TMP_PATH = "data/tmp/";

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");

    @Override
    public RepeatStatus execute(@NonNull StepContribution contribution, ChunkContext chunkContext) throws IOException {
        JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobParameters();

        String publicationId = jobParameters.getString(PUBLICATION_ID);
        String exportFormat = jobParameters.getString(EXPORT_FORMAT);

        Path directoryPath = Path.of(TMP_PATH + buildDirectoryName(publicationId, exportFormat));

        Files.createDirectories(directoryPath);

        chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put(DIRECTORY, directoryPath.toString());

        return RepeatStatus.FINISHED;
    }

    private String buildDirectoryName(String publicationId, String exportFormat) {
        String publicationGuid = publicationId.substring(5);

        return publicationGuid + "_" + exportFormat + "_" + formatter.format(LocalDateTime.now());
    }
}
