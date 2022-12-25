package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.tasklet;

import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.ExecutionContextKey.DIRECTORY;
import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.ExecutionContextKey.ZIPPED_FILE;

@Component
@StepScope
public class CleanUpExportTasklet implements Tasklet {

    private Path directory;

    private Path zippedFile;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Files.delete(zippedFile);
        FileUtils.deleteDirectory(directory.toFile());

        return RepeatStatus.FINISHED;
    }

    @Autowired
    public void setDirectory(@Value("#{jobExecutionContext['" + DIRECTORY + "']}") String directory) {
        this.directory = Path.of(directory);
    }

    @Autowired
    public void setZippedFile(@Value("#{jobExecutionContext['" + ZIPPED_FILE + "']}") String zippedFile) {
        this.zippedFile = Path.of(zippedFile);
    }
}
