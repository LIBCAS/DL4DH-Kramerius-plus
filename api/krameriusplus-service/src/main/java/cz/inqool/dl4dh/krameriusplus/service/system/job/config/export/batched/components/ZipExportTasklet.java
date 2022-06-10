package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.batched.components;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobParameterKey.DIRECTORY;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobParameterKey.ZIPPED_FILE;

@Component
@StepScope
public class ZipExportTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(@NonNull StepContribution contribution, @NonNull ChunkContext chunkContext) throws Exception {
        String directory = (String) chunkContext.getStepContext().getJobExecutionContext().get(DIRECTORY);
        Path directoryToZip = Path.of(directory);

        Path zippedFile = Files.createFile(Path.of(chunkContext.getStepContext().getJobExecutionContext().get(DIRECTORY) + ".zip"));

        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zippedFile))) {
            Files.walk(directoryToZip)
                    .filter(file -> !Files.isDirectory(file))
                    .forEach(file -> {
                        ZipEntry zipEntry = new ZipEntry(directoryToZip.relativize(file).toString());
                        try {
                            zos.putNextEntry(zipEntry);
                            Files.copy(file, zos);
                            zos.closeEntry();
                        } catch (IOException e) {
                            throw new UncheckedIOException("Error when zipping directory '" + directoryToZip.toString() + "'.", e);
                        }
                    });
        }

        chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put(ZIPPED_FILE, zippedFile.toString());

        return RepeatStatus.FINISHED;
    }
}
