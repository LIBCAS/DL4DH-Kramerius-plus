package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.DigitalObjectWithPathDto;
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

    private final static String PAGE_NAME_FORMAT = "page%04d_%s.%s";

    protected OutputStream getItemOutputStream(DigitalObjectWithPathDto item) throws IOException {
        Files.createDirectories(Path.of(item.getPath()));
        return Files.newOutputStream(Path.of(item.getPath(), getItemFileName(item)));
    }

    protected abstract String getItemFileName(DigitalObjectWithPathDto item);

    protected String getPageFilename(Page page, String fileExtension) {
        Integer pageNumber = page.getIndex();
        String pageId = page.getId().substring(5);
        return String.format(PAGE_NAME_FORMAT, pageNumber, pageId, fileExtension);
    }

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
