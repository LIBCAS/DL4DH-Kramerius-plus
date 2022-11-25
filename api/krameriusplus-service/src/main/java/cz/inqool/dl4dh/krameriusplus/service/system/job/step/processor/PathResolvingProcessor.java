package cz.inqool.dl4dh.krameriusplus.service.system.job.step.processor;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.dto.DigitalObjectWithPathDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.DIRECTORY;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ID;
import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

@Component
@StepScope
public class PathResolvingProcessor implements ItemProcessor<DigitalObject, DigitalObjectWithPathDto> {

    private final PublicationStore publicationStore;

    private final Map<String, String> parentCache = new HashMap<>();

    private final String exportDirectory;

    private final String root;

    @Autowired
    public PathResolvingProcessor(PublicationStore publicationStore,
                                  @Value("#{jobParameters['" + PUBLICATION_ID + "']}") String publicationId,
                                  @Value("#{jobExecutionContext['" + DIRECTORY + "']}") String exportDirectory) {
        notNull(exportDirectory, () -> new IllegalStateException("Export context missing export directory"));
        this.publicationStore = publicationStore;
        this.exportDirectory = exportDirectory;
        this.parentCache.put(publicationId, null);
        this.root = publicationId;
    }

    @Override
    public DigitalObjectWithPathDto process(DigitalObject item) throws Exception {
        DigitalObjectWithPathDto result = new DigitalObjectWithPathDto();
        result.setDigitalObject(item);
        result.setPath(resolvePath(item));
        return result;
    }

    private String resolvePath(DigitalObject digitalObject) {
        return digitalObject instanceof Page ?
                resolvePath(((Page) digitalObject)) : resolvePath(((Publication) digitalObject));
    }

    private String resolvePath(Page page) {
        return buildPathFromCache(page.getParentId());
    }

    private String resolvePath(Publication publication) {
        if (!parentCache.containsKey(publication.getId())) {
            cacheTreeBranch(publication);
        }

        return buildPathFromCache(publication.getId());
    }

    private void cacheTreeBranch(Publication publication) {
        while (publication != null && publication.getParentId() != null) {
            parentCache.putIfAbsent(publication.getId(), publication.getParentId());
            publication = publicationStore.findById(publication.getParentId()).orElse(null);
        }
    }

    private String buildPathFromCache(String id) {
        if (!parentCache.containsKey(id)) {
            cacheTreeBranch(publicationStore.findById(id).orElse(null));
        }

        Path result = Path.of(".");
        while (!root.equals(id)) {
            result = Path.of(id != null ? stripUuid(id) : "" , result.toString());
            id = parentCache.get(id);

        }

        return Path.of(exportDirectory, result.toString()).toString();
    }

    private String stripUuid(String id) {
        return id.substring(5);
    }
}
