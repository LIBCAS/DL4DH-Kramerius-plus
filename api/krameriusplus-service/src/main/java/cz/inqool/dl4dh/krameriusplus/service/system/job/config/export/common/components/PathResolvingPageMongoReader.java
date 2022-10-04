package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObjectContext;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.DataProvider;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.DigitalObjectWithPathDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.reader.PageMongoReader;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.DIRECTORY;

@Component
@StepScope
public class PathResolvingPageMongoReader implements ItemReader<DigitalObjectWithPathDto>, StepExecutionListener {

    private final PageMongoReader pageMongoReader;

    private Path currentPath;

    private Path exportDirectory;

    private final DataProvider dataProvider;

    @Autowired
    public PathResolvingPageMongoReader(PageMongoReader pageMongoReader, DataProvider dataProvider) {
        this.pageMongoReader = pageMongoReader;
        this.dataProvider = dataProvider;
    }

    @Override
    public DigitalObjectWithPathDto read() throws Exception {
        Page page = pageMongoReader.read();
        if (page == null) {
            return null;
        }

        DigitalObjectWithPathDto result = new DigitalObjectWithPathDto();

        String parent = stripUuid(page.getParentId());

        if (currentPath == null) {
            resolveDirectory(page);
        }
        else if (!parent.equals(getParentDir())) {
            currentPath = null;
            resolveDirectory(page);
        }

        result.setPath(currentPath.toString());
        result.setDigitalObject(page);
        return result;
    }

    private void resolveDirectory(Page page) {
        List<DigitalObjectContext> pageContext =  dataProvider.getDigitalObject(page.getId()).getContext();
        currentPath = exportDirectory;
        for (DigitalObjectContext digitalObjectContext : pageContext) {
                currentPath = currentPath.resolve(Path.of(stripUuid(digitalObjectContext.getPid())));
        }
        currentPath = currentPath.getParent();
    }

    private String getParentDir() {
        return currentPath.getParent().toString();
    }

    private String stripUuid(String id) {
        return id.substring(5);
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        exportDirectory = Path.of(stepExecution.getJobExecution().getExecutionContext().getString(DIRECTORY));
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
