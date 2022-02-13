package cz.inqool.dl4dh.krameriusplus.core.system.enricher;

import cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.kramerius.DataProvider;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.PageStore;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.CompletePageEnricher;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.publication.PublicationEnricher;
import cz.inqool.dl4dh.krameriusplus.core.system.scheduling.EnrichmentTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.system.scheduling.EnrichmentState.SUCCESSFUL;

/**
 * @author Norbert Bodnar
 */
@Component
@Slf4j
public class Enricher {

    private final PublicationEnricher publicationEnricher;
    private final CompletePageEnricher completePageEnricher;
    private final PublicationStore publicationStore;
    private final PageStore pageStore;
    private final DataProvider dataProvider;

    @Autowired
    public Enricher(PublicationEnricher publicationEnricher, CompletePageEnricher completePageEnricher,
                    PublicationStore publicationStore, PageStore pageStore, DataProvider dataProvider) {
        this.publicationEnricher = publicationEnricher;
        this.completePageEnricher = completePageEnricher;
        this.publicationStore = publicationStore;
        this.pageStore = pageStore;
        this.dataProvider = dataProvider;
    }

    public void enrich(Publication publication, EnrichmentTask.EnrichmentSubTask task) {
        assemblePublication(publication, task);
        enrichOne(publication, task);

        for (DigitalObject child : publication.getChildren()) {
            if (child instanceof Publication) {
                enrich((Publication) child, findSubtaskForPublication(task, child.getId()));
            }
        }

        finishTask(task);
    }

    private EnrichmentTask.EnrichmentSubTask findSubtaskForPublication(EnrichmentTask.EnrichmentSubTask task, String publicationId) {
        return task.getSubtasks()
                .stream()
                .filter(subtask -> subtask.getPublicationId().equals(publicationId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing subtask for publication with id " + publicationId));
    }

    private void enrichOne(Publication publication, EnrichmentTask.EnrichmentSubTask task) {
        publicationEnricher.enrich(publication);
        completePageEnricher.enrich(publication.getPages(), task);

        fillParadata(publication);

        publicationStore.save(publication);
        pageStore.save(publication.getPages());
    }

    private void assemblePublication(Publication publication, EnrichmentTask.EnrichmentSubTask task) {
        // get children
        List<DigitalObject> childObjects = dataProvider.getDigitalObjectsForParent(publication);

        // separate into pages and digitalObjects
        for (DigitalObject child : childObjects) {
            if (child instanceof Page) {
                publication.getPages().add((Page) child);
                task.setTotalPages(task.getTotalPages() + 1);
            } else {
                publication.getChildren().add(child);
                task.getSubtasks().add(new EnrichmentTask.EnrichmentSubTask(child.getId()));
            }
        }
    }

    private void fillParadata(Publication publication) {
        new ParadataFiller(publication).fill();
    }

    private void finishTask(EnrichmentTask.EnrichmentSubTask task) {
        log.info("Enrichment of publication with ID={} finished", task.getPublicationId());
        task.setFinished(Instant.now());
        task.setState(SUCCESSFUL);
    }
}
