package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Paging;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.store.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.store.PageStore;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.dto.PublicationListFilterDto;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.store.PublicationStore;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.eq;
import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author Norbert Bodnar
 */
@Service
@Slf4j
public class PublicationService {

    private PublicationStore publicationStore;

    private PageStore pageStore;

    @Transactional
    public void save(@NonNull Publication publication) {
        publicationStore.save(publication);
    }

    public List<Publication> findAllPublishedModified(Instant publishedModifiedAfter) {
        return publicationStore.findAllPublishedModified(publishedModifiedAfter);
    }

    @Transactional
    public void publish(String publicationId) {
        Optional<Publication> publicationOptional = publicationStore.findById(publicationId);
        if (publicationOptional.isEmpty()) {
            throw new MissingObjectException(Publication.class, publicationId);
        }

        Publication publication = publicationOptional.get();
        publication.getPublishInfo().publish();

        publicationStore.save(publication);
    }

    @Transactional
    public void unPublish(String publicationId) {
        Optional<Publication> publicationOptional = publicationStore.findById(publicationId);
        if (publicationOptional.isEmpty()) {
            throw new MissingObjectException(Publication.class, publicationId);
        }

        Publication publication = publicationOptional.get();
        publication.getPublishInfo().unPublish();

        publicationStore.save(publication);
    }

    /**
     * Returns the publication with given ID with all its fields
     */
    public Publication find(String publicationId) {
        Publication publication = publicationStore.findById(publicationId).orElse(null);
        notNull(publication, () -> new MissingObjectException(Publication.class, publicationId));

        return publication;
    }

    public Page findPage(String publicationId, String pageId) {
        Page page = pageStore.findById(pageId).orElse(null);
        notNull(page, () -> new MissingObjectException(Page.class, pageId));
        eq(page.getParentId(), publicationId, () -> new MissingObjectException(Page.class, pageId));

        return page;
    }

    public QueryResults<Publication> findAllChildren(String publicationId, int page, int pageSize) {
        return publicationStore.findAllChildren(publicationId, PageRequest.of(page, pageSize));
    }

    public QueryResults<Page> findAllPages(String publicationId) {
        return pageStore.findAllByPublication(publicationId, Pageable.unpaged());
    }

    public List<Page> findAllPages(String publicationID, Params params) {
        Query query = params.toMongoQuery(false);
        query.addCriteria(where("parentId").is(publicationID));
        query.with(Sort.by(Sort.Order.asc("index")));

        return pageStore.findAll(query);
    }

    public QueryResults<Page> findAllPages(String publicationId, int page, int pageSize) {
        return pageStore.findAllByPublication(publicationId, PageRequest.of(page, pageSize));
    }

    public QueryResults<Publication> findAll(PublicationListFilterDto filter, int page, int pageSize) {
        Params params = filter.toParams();
        params.setPaging(Paging.of(page, pageSize));

        return publicationStore.findAll(params);
    }

    @Autowired
    public void setPublicationStore(PublicationStore publicationStore) {
        this.publicationStore = publicationStore;
    }

    @Autowired
    public void setPageStore(PageStore pageStore) {
        this.pageStore = pageStore;
    }
}
