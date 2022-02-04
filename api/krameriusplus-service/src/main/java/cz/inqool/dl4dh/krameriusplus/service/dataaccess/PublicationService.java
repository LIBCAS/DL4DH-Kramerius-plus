package cz.inqool.dl4dh.krameriusplus.service.dataaccess;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.internalpart.InternalPart;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.internalpart.InternalPartStore;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.PageStore;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.publication.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.domain.entity.scheduling.EnrichmentTask;
import cz.inqool.dl4dh.krameriusplus.domain.entity.scheduling.EnrichmentTaskStore;
import cz.inqool.dl4dh.krameriusplus.domain.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.domain.params.Params;
import cz.inqool.dl4dh.krameriusplus.domain.params.filter.EqFilter;
import cz.inqool.dl4dh.krameriusplus.domain.params.filter.Sorting;
import cz.inqool.dl4dh.krameriusplus.service.dataprovider.DataProvider;
import cz.inqool.dl4dh.krameriusplus.service.enricher.Enricher;
import cz.inqool.dl4dh.krameriusplus.service.enricher.ParadataFiller;
import cz.inqool.dl4dh.krameriusplus.service.scheduler.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.Future;

import static cz.inqool.dl4dh.krameriusplus.domain.entity.scheduling.EnrichmentTask.State.CANCELED;
import static cz.inqool.dl4dh.krameriusplus.domain.entity.scheduling.EnrichmentTask.State.FAILED;
import static cz.inqool.dl4dh.krameriusplus.domain.exception.ExceptionUtils.notNull;

/**
 * @author Norbert Bodnar
 */
@Service
@Slf4j
public class PublicationService {

    private final PublicationStore publicationStore;

    private final PageStore pageStore;

    private final InternalPartStore internalPartStore;

    private final Enricher enricher;

    private final DataProvider dataProvider;

    private final EnrichmentTaskStore enrichmentTaskStore;

    @Autowired
    public PublicationService(PublicationStore publicationStore, PageStore pageRepository, InternalPartStore internalPartRepository,
                              Enricher enricher, DataProvider dataProvider, EnrichmentTaskStore enrichmentTaskStore) {
        this.publicationStore = publicationStore;
        this.pageStore = pageRepository;
        this.internalPartStore = internalPartRepository;
        this.enricher = enricher;
        this.dataProvider = dataProvider;
        this.enrichmentTaskStore = enrichmentTaskStore;
    }

    @Async
    public Future<String> enrichPublication(String id, EnrichmentTask task) {
        try {
            DigitalObject digitalObject = dataProvider.getDigitalObject(id);
            if (!(digitalObject instanceof Publication)) {
                throw new IllegalArgumentException("Provided ID is not an ID of a Digital Object of type Publication");
            }

            enrich((Publication) digitalObject, task);
            finishTask(task);
        } catch (Exception e) {
            if (e.getCause() instanceof InterruptedException) {
                task.setErrorMessage("Interrupted");
                task.setState(CANCELED);
            } else {
                task.setErrorMessage(e.getMessage());
                task.setState(FAILED);
            }
            log.error("Task wid PID=" + id + " failed", e);

            enrichmentTaskStore.create(task);
            SchedulerService.removeTask(id);
        }

        return new AsyncResult<>("done");
    }

    private void enrich(Publication publication, EnrichmentTask task) {
        // get children
        List<DigitalObject> childObjects = dataProvider.getDigitalObjectsForParent(publication);

        // separate into pages and digitalObjects
        for (DigitalObject child : childObjects) {
            if (child instanceof Page) {
                publication.getPages().add((Page) child);
            } else {
                publication.getChildren().add(child);
            }
        }

        // enrich publication
        enricher.enrich(publication);
        // enrich pages
        enricher.enrich(publication.getPages(), task);
        // save pages
        pageStore.create(publication.getPages());
        // fill paradata from pages
        fillParadata(publication); //TODO: should this be saved at publication level?

        // enrich all children publications
        enrichPublicationChildren(publication, task);

        publicationStore.create(publication);
    }


    private void enrichPublicationChildren(Publication publication, EnrichmentTask task) {
        for (DigitalObject child : publication.getChildren()) {
            if (child instanceof Publication) {
                enrich((Publication) child, task);
            } else if (child instanceof InternalPart) {
                internalPartStore.create((InternalPart) child);
            }
        }
    }

    private void fillParadata(Publication publication) {
        new ParadataFiller(publication).fill();
    }

    private void finishTask(EnrichmentTask task) {
        task.setFinished(Instant.now());
        task.setState(EnrichmentTask.State.SUCCESSFUL);

        enrichmentTaskStore.create(task);

        log.info("Enrichment finished in " + task.getTook());
        SchedulerService.removeTask(task.getPublicationId());
    }

    /**
     * Returns the publication with given ID with all its fields
     */
    public Publication find(String publicationId) {
        Publication publication = publicationStore.find(publicationId);
        notNull(publication, () -> new MissingObjectException(Publication.class, publicationId));

        return publication;
    }

    /**
     * Returns a publication with given ID, including its pages depending on the given {@param param}
     */
    public Publication findWithPages(String publicationId, Params params) {
        Publication publication = find(publicationId);

        params.addFilters(new EqFilter("parentId", publicationId));

        if (params.getSorting().isEmpty()) {
            params.getSorting().add(new Sorting("index", Sort.Direction.ASC));
        }

        publication.setPages(pageStore.list(params));

        return publication;
    }

    public List<Publication> list(Params params) {
        return publicationStore.list(params);
    }
}
