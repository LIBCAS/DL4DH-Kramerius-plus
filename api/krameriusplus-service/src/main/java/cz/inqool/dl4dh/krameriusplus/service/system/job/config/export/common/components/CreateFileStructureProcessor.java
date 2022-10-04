package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.PageWithPathDto;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.DIRECTORY;

@Component
@StepScope
public class CreateFileStructureProcessor implements ItemProcessor<DigitalObject, PageWithPathDto>, StepExecutionListener {

    private Path currentDir;

    @Override
    public PageWithPathDto process(DigitalObject item) throws Exception {
        if (item instanceof Page) {
            PageWithPathDto result = new PageWithPathDto();
            result.setPage(((Page) item));
            result.setPath(currentDir.toString());

            return result;
        }

        if (item instanceof Publication) {
            createDir(((Publication) item));

            return null;
        }

        return null;
    }

    private void createDir(Publication publication) throws IOException {
        String publicationDirName = removeUuid(publication.getId());

        // in case of root just make the directory
        if (publication.getParentId() == null) {
            currentDir = Path.of(currentDir.toString(), publicationDirName);
            Files.createDirectory(currentDir);
            return;
        }

        // go to parent directory
        while (!currentDir.getFileName().toString().equals(removeUuid(publication.getParentId()))) {
            currentDir = currentDir.getParent();
        }

        // go to current publication directory
        currentDir = Path.of(currentDir.toString(), publicationDirName);
        Files.createDirectory(currentDir);
    }

    private String removeUuid(String id) {
        return id.substring(5);
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        currentDir = Path.of(stepExecution.getJobExecution().getExecutionContext().getString(DIRECTORY));
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
