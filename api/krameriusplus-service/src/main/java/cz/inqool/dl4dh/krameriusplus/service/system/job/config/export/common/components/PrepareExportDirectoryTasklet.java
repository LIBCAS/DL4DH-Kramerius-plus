package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components;

import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.DIRECTORY;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.KRAMERIUS_JOB;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ID;

@Component
@StepScope
public class PrepareExportDirectoryTasklet extends ValidatedTasklet {

    public static final String TMP_PATH = "data/tmp/";

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");

    @Override
    protected RepeatStatus executeValidatedTasklet(@NonNull StepContribution contribution, ChunkContext chunkContext) throws IOException {
        JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobParameters();

        String publicationId = jobParameters.getString(PUBLICATION_ID);
        KrameriusJob krameriusJob = KrameriusJob.valueOf(jobParameters.getString(KRAMERIUS_JOB));

        Path directoryPath = Path.of(TMP_PATH + buildDirectoryName(publicationId, krameriusJob));

        Files.createDirectories(directoryPath);

        chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put(DIRECTORY, directoryPath.toString());

        return RepeatStatus.FINISHED;
    }

    private String buildDirectoryName(String publicationId, KrameriusJob krameriusJob) {
        String publicationGuid = publicationId.substring(5);

        return krameriusJob.name() + "_" + publicationGuid + "_" + formatter.format(LocalDateTime.now());
    }
}
