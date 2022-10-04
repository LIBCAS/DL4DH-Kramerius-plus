package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObjectContext;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.DataProvider;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.DigitalObjectWithPathDto;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.DIRECTORY;

public abstract class FileWriter<T> implements ItemWriter<T> {

    protected Path exportDirectory;

    private DataProvider dataProvider;

    private final static String PAGE_NAME_FORMAT = "page%04d_%s.%s";

    protected OutputStream getItemOutputStream(DigitalObjectWithPathDto item) throws IOException {
        return Files.newOutputStream(Path.of(item.getPath(), getItemFileName(item)));
    }

    protected abstract String getItemFileName(DigitalObjectWithPathDto item);

    protected String getPageFilename(Page page, String fileExtension) {
        Integer pageNumber = page.getIndex();
        String pageId = page.getId().substring(5);
        return String.format(PAGE_NAME_FORMAT, pageNumber, pageId, fileExtension);
    }

    protected String findPublicationDirectory(Publication publication) throws IOException {
        Publication publicationWithContext = ((Publication) dataProvider.getDigitalObject(publication.getId()));
        Path publicationPath = exportDirectory;

        for (DigitalObjectContext digitalObjectContext : publicationWithContext.getContext()) {
            publicationPath = publicationPath.resolve(digitalObjectContext.getPid().substring(5));
        }

        Files.createDirectories(publicationPath);
        return publicationPath.toString();
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

    @Autowired
    public void setDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }
}
