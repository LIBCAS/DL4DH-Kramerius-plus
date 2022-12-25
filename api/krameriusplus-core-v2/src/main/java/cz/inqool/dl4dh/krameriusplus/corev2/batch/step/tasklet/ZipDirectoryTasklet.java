package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.tasklet;

import cz.inqool.dl4dh.krameriusplus.corev2.utils.ZipArchiver;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.ExecutionContextKey.DIRECTORY;
import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.ExecutionContextKey.ZIPPED_FILE;

@Component
@StepScope
public class ZipDirectoryTasklet implements Tasklet {

    private ZipArchiver zipArchiver;

    private Path directory;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Path zippedFilePath = directory.getParent().resolve(directory.getFileName() + ".zip");

        zipArchiver.zip(directory, zippedFilePath);

        chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().putString(ZIPPED_FILE, zippedFilePath.toString());

        return RepeatStatus.FINISHED;
    }

    @Autowired
    public void setZipArchiver(ZipArchiver zipArchiver) {
        this.zipArchiver = zipArchiver;
    }

    @Autowired
    public void setDirectory(@Value("#{jobExecutionContext['" + DIRECTORY + "']}") String directory) {
        this.directory = Path.of(directory);
    }
}
