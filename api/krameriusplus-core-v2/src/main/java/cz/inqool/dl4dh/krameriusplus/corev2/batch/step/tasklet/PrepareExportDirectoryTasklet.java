package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.tasklet;

import cz.inqool.dl4dh.krameriusplus.api.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.corev2.job.config.export.ExportJobConfig;
import cz.inqool.dl4dh.krameriusplus.corev2.job.config.export.ExportJobConfigStore;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.ExecutionContextKey.DIRECTORY;
import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.JOB_CONFIG_ID;
import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.PUBLICATION_ID;
import static cz.inqool.dl4dh.krameriusplus.corev2.utils.Utils.notNull;

@Component
@StepScope
public class PrepareExportDirectoryTasklet implements Tasklet {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private Path tmpDirPath;

    @Value("#{jobParameters['" + PUBLICATION_ID + "']}")
    private String publicationId;

    @Value("#{jobParameters['" + JOB_CONFIG_ID + "']}")
    private String jobConfigId;

    private ExportJobConfigStore jobConfigStore;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws IOException {
        ExportJobConfig jobConfig = jobConfigStore.findById(jobConfigId)
                .orElseThrow(() -> new MissingObjectException(ExportJobConfig.class, jobConfigId));
        notNull(jobConfig, () -> new MissingObjectException(ExportJobConfig.class, jobConfigId));

        Path directory = tmpDirPath.resolve(buildDirectoryName(publicationId, jobConfig.getExportFormat()));
        Files.createDirectories(directory);

        contribution.getStepExecution().getJobExecution().getExecutionContext().putString(DIRECTORY, directory.toString());

        return RepeatStatus.FINISHED;
    }

    private String buildDirectoryName(String publicationId, ExportFormat exportFormat) {
        String publicationGuid = publicationId.substring(5);

        return "EXPORT_" + exportFormat + "_" + "UUID_" + publicationGuid + "_" + formatter.format(LocalDateTime.now());
    }

    @Autowired
    public void setTmpDirPath(@Value("${system.export.tmp-dir}") String tmpDirPath) {
        this.tmpDirPath = Path.of(tmpDirPath);
    }

    @Autowired
    public void setJobConfigStore(ExportJobConfigStore jobConfigStore) {
        this.jobConfigStore = jobConfigStore;
    }
}
