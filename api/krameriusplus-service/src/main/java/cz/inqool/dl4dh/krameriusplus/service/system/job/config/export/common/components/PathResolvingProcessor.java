package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto.DigitalObjectWithPathDto;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.DIRECTORY;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ID;
import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

@Component
@StepScope
public class PathResolvingProcessor implements ItemProcessor<DigitalObject, DigitalObjectWithPathDto>, StepExecutionListener {

    private final PublicationStore publicationStore;

    private final Map<String, String> parentCache = new HashMap<>();

    private String exportDirectory;

    @Autowired
    public PathResolvingProcessor(PublicationStore publicationStore) {
        this.publicationStore = publicationStore;
    }

    @Override
    public DigitalObjectWithPathDto process(DigitalObject item) throws Exception {
        DigitalObjectWithPathDto result = new DigitalObjectWithPathDto();
        result.setDigitalObject(item);
        result.setPath(resolvePath(item));
        return result;
    }

    private String resolvePath(DigitalObject digitalObject) throws IOException {
        return digitalObject instanceof Page ?
                resolvePath(((Page) digitalObject)) : resolvePath(((Publication) digitalObject));
    }

    private String resolvePath(Page page) {
        return buildPathFromCache(page.getParentId());
    }

    private String resolvePath(Publication publication) throws IOException {
        if (!parentCache.containsKey(publication.getId())) {
            cacheTreeBranch(publication);
        }

        String directory = buildPathFromCache(publication.getId());
        Files.createDirectory(Path.of(directory));
        return directory;
    }

    private void cacheTreeBranch(Publication publication) {
        while (publication.getParentId() != null) {
            parentCache.put(publication.getId(), publication.getParentId());
            publication = publicationStore.findById(publication.getParentId())
                    .orElseThrow(() -> new IllegalStateException("if parent id exists then parent exists too"));
        }
    }

    private String buildPathFromCache(String id) {
        if (!parentCache.containsKey(id)) {
            cacheTreeBranch(publicationStore.findById(id).orElseThrow(() -> new IllegalStateException("if parent id exists then parent exists too")));
        }

        Path result = Path.of(stripUuid(id));
        while (id != null) {
            id = parentCache.get(id);
            result = Path.of(id != null ? stripUuid(id) : "" , result.toString());
        }

        return Path.of(exportDirectory, result.toString()).toString();
    }

    private String stripUuid(String id) {
        return id.substring(5);
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        exportDirectory = stepExecution.getJobExecution().getExecutionContext().getString(DIRECTORY);
        parentCache.put(stepExecution.getJobParameters().getString(PUBLICATION_ID), null);
        notNull(exportDirectory, () -> new IllegalStateException("export context missing export directory"));

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
