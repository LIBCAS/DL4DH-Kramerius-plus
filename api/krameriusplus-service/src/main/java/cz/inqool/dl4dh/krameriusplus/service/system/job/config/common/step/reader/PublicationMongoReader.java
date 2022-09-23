package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.reader;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.store.PublicationStore;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.AbstractPaginatedDataItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ID;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
@StepScope
public class PublicationMongoReader extends AbstractPaginatedDataItemReader<Publication> {

    private static final int Q_PAGE_SIZE = 100;

    private final PublicationStore publicationStore;

    private String currentParentId;

    private boolean fetchRoot = true;

    private final String rootId;

    private final Deque<String> parentIds = new LinkedList<>();

    @Autowired
    public PublicationMongoReader(PublicationStore publicationStore,
                                  @Value("#{jobParameters['" + PUBLICATION_ID + "']}") String publicationId) {
        this.publicationStore = publicationStore;
        setName(PublicationMongoReader.class.getSimpleName());
        this.pageSize = Q_PAGE_SIZE;
        this.rootId = publicationId;


        currentParentId = publicationId;
    }

    @Override
    protected Iterator<Publication> doPageRead() {
        if (currentParentId == null) {
            if (fetchRoot) {
                fetchRoot = false;
                return List.of(publicationStore.findById(rootId).orElseThrow(() -> new IllegalStateException("root not found in db"))).iterator();
            }
            else {
                return null;
            }
        }

        List<Publication> publications = publicationStore.findAll(buildQuery());

        if (publications.isEmpty()) {
            fetchCurrentPublicationChildPublications();
            currentParentId = popParentId();
            this.page = 0;


            return doPageRead();
        }

        return publications.iterator();
    }

    private Query buildQuery() {
        Query query = new Query();
        query.addCriteria(where("parentId").is(currentParentId));
        query.with(PageRequest.of(page, pageSize)).with(Sort.by("index").ascending());

        return query;
    }

    private void fetchCurrentPublicationChildPublications() {
        publicationStore.findAllChildrenIds(currentParentId).forEach(parentIds::push);
    }

    private String popParentId() {
        try {
            return parentIds.pop();
        } catch (EmptyStackException e) {
            return null;
        }
    }
}
