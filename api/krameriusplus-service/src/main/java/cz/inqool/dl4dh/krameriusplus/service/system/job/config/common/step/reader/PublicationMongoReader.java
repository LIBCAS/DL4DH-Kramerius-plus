package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.reader;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.core.utils.JsonUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.AbstractPaginatedDataItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PARAMS;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ID;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
@StepScope
public class PublicationMongoReader extends AbstractPaginatedDataItemReader<Publication> {

    private static final int Q_PAGE_SIZE = 100;

    private final PublicationStore publicationStore;

    private String currentParentId;

    private final String rootId;

    private final Stack<String> parentIds = new Stack<>();

    private final Params params;

    private final int pageSize;

    @Autowired
    public PublicationMongoReader(PublicationStore publicationStore,
                                  @Value("#{jobParameters['" + PUBLICATION_ID + "']}") String publicationId,
                                  @Value("#{jobParameters['" + PARAMS + "']}") String stringParams) {
        this.publicationStore = publicationStore;
        setName(PublicationMongoReader.class.getSimpleName());
        this.pageSize = Q_PAGE_SIZE;

        rootId = publicationId;
        currentParentId = publicationId;

        if (stringParams != null) {
            this.params = JsonUtils.fromJsonString(stringParams, Params.class);
        }
        else {
            this.params = new Params();
        }


    }

    @Override
    protected Iterator<Publication> doPageRead() {
        if (currentParentId == null) {
            return null;
        }

        List<Publication> publications = publicationStore.findAll(buildQuery());

        if (publications.isEmpty()) {
            fetchCurrentPublicationChildPublications();
            currentParentId = popParentId();

            return doPageRead();
        }

        return publications.iterator();
    }

    private Query buildQuery() {
        Query query = params.toMongoQuery(false);
        if (currentParentId.equals(rootId)) {
            query.addCriteria(new Criteria().orOperator(where("_id").is(rootId), where("parentId").is(rootId)));
            fetchCurrentPublicationChildPublications();
            currentParentId = popParentId();
        }
        else {
            query.addCriteria(where("parentId").is(currentParentId));
        }
        query.with(PageRequest.of(page, pageSize)).with(Sort.by("index").ascending());

        return query;
    }

    private void fetchCurrentPublicationChildPublications() {
        parentIds.addAll(publicationStore.findAllChildrenIds(currentParentId));
    }

    private String popParentId() {
        try {
            return parentIds.pop();
        } catch (EmptyStackException e) {
            return null;
        }
    }
}
