package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.store.PageStore;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.dto.PublicationListFilterDto;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.store.PublicationStore;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.eq;
import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

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

    /**
     * Returns the given publication with only teiHeader and teiBody fields
     * @param publicationId
     * @return
     */
    public Publication listTei(String publicationId) {
        Publication result = publicationStore.listWithTei(publicationId);

        result.setPages(pageStore.listWithTei(publicationId));

        return result;
    }

    public List<Publication> listPublishedModified(Instant publishedModifiedAfter) {
        return publicationStore.listPublishedModified(publishedModifiedAfter);
    }

    @Transactional
    public void publish(String publicationId) {
        Publication publication = publicationStore.find(publicationId);

        publication.getPublishInfo().publish();

        publicationStore.save(publication);
    }

    @Transactional
    public void unPublish(String publicationId) {
        Publication publication = publicationStore.find(publicationId);

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

    public QueryResults<Publication> listChildren(String publicationId, int page, int pageSize) {
        return publicationStore.list(publicationId, page, pageSize);
    }

    public QueryResults<Page> listPages(String publicationId, int page, int pageSize) {
        return pageStore.list(publicationId, page, pageSize);
    }

    public QueryResults<Publication> list(PublicationListFilterDto filter, int page, int pageSize) {
        return publicationStore.list(filter, page, pageSize);
    }

    public List<Publication> list(Params params) {
        //return publicationStore.listAll(params);
        return null;
    }

    public String getTitle(String publicationId) {
        return publicationStore.getTitle(publicationId);
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
