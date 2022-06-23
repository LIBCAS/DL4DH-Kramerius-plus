package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.DIRECTORY;

public abstract class FileWriter<T> implements ItemWriter<T> {

    protected Path exportDirectory;

    protected OutputStream getItemOutputStream(DigitalObject item) throws IOException {
        return Files.newOutputStream(exportDirectory.resolve(Path.of(getItemFileName(item))));
    }

    protected abstract String getItemFileName(DigitalObject item);

    /**
     * Can be overriden to inject objects from context
     * @param stepExecution
     */
    protected void doBeforeStep(StepExecution stepExecution) {
        // do nothing
    }

    @BeforeStep
    public void injectExecutionContext(StepExecution stepExecution) {
        String path = stepExecution.getJobExecution().getExecutionContext().getString(DIRECTORY);
        exportDirectory = Path.of(path);

        doBeforeStep(stepExecution);
    }
}
