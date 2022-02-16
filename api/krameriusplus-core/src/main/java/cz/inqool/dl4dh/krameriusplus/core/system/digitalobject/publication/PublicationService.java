package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication;

import cz.inqool.dl4dh.krameriusplus.core.domain.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.domain.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.domain.params.filter.EqFilter;
import cz.inqool.dl4dh.krameriusplus.core.domain.params.filter.Sorting;
import cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.kramerius.DataProvider;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.internalpart.InternalPartStore;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.PageStore;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.Enricher;
import cz.inqool.dl4dh.krameriusplus.core.system.scheduling.EnrichmentTask;
import cz.inqool.dl4dh.krameriusplus.core.system.scheduling.EnrichmentTaskStore;
import cz.inqool.dl4dh.krameriusplus.core.system.scheduling.SchedulerService;
import cz.inqool.dl4dh.krameriusplus.core.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

import static cz.inqool.dl4dh.krameriusplus.core.system.scheduling.EnrichmentState.*;

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
            task.setState(DOWNLOADING);
            DigitalObject digitalObject = dataProvider.getDigitalObject(id);

            if (!(digitalObject instanceof Publication)) {
                throw new IllegalArgumentException("Provided ID is not an ID of a Digital Object of type Publication");
            }
            enricher.enrich((Publication) digitalObject, task.getSubtask());
        } catch (Exception exception) {
            if (exception.getCause() instanceof InterruptedException) {
                task.setErrorMessage("Interrupted");
                task.setState(CANCELED);
            } else {
                task.setErrorMessage(exception.getMessage());
                task.setState(FAILED);
            }
            log.error("Task wid PID=" + id + " failed", exception);
        }

        enrichmentTaskStore.update(task);
        SchedulerService.removeTask(id);

        return new AsyncResult<>("done");
    }

    /**
     * Returns the publication with given ID with all its fields
     */
    public Publication find(String publicationId) {
        Publication publication = publicationStore.find(publicationId);
        Utils.notNull(publication, () -> new MissingObjectException(Publication.class, publicationId));

        return publication;
    }

    /**
     * Returns a publication with given ID, including its pages depending on the given {@param param}
     */
    public Publication findWithPages(String publicationId, Params pageParams) {
        Publication publication = find(publicationId);

        pageParams.addFilters(new EqFilter("parentId", publicationId));

        if (pageParams.getSorting().isEmpty()) {
            pageParams.getSorting().add(new Sorting("index", Sort.Direction.ASC));
        }

        publication.setPages(pageStore.listAll(pageParams));

        return publication;
    }

    public List<Publication> list(Params params) {
        return publicationStore.listAll(params);
    }
}
